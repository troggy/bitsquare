/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.coloredcoins;

import io.bitsquare.coloredcoins.providers.ColoredCoinMetadata;
import io.bitsquare.coloredcoins.providers.ColoredCoinsDataProvider;
import io.bitsquare.common.crypto.KeyRing;
import io.bitsquare.http.HttpException;
import io.bitsquare.locale.CurrencyUtil;
import io.bitsquare.p2p.P2PService;
import io.bitsquare.p2p.storage.HashMapChangedListener;
import io.bitsquare.p2p.storage.storageentry.ProtectedStorageEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class ColoredCoinsService {

    private static final Logger log = LoggerFactory.getLogger(ColoredCoinsService.class);

    private P2PService p2PService;
    private final KeyRing keyRing;
    private final ColoredCoinsDataProvider coloredCoinsDataProvider;
    private final Set<NewColoredCoinAddedListener> listeners = new CopyOnWriteArraySet<>();

    @Inject
    public ColoredCoinsService(P2PService p2PService, KeyRing keyRing, ColoredCoinsDataProvider coloredCoinsDataProvider) {
        this.keyRing = keyRing;
        this.p2PService = p2PService;
        this.coloredCoinsDataProvider = coloredCoinsDataProvider;

        p2PService.addHashSetChangedListener(new HashMapChangedListener() {
            @Override
            public void onAdded(ProtectedStorageEntry data) {
                if (data.getStoragePayload() instanceof ColoredCoinCurrency) {
                    ColoredCoinCurrency coloredCoinCurrency = (ColoredCoinCurrency) data.getStoragePayload();
                    System.out.println("Adding");
                    log.debug("Colored coin added message: " + coloredCoinCurrency);
                    if (!CurrencyUtil.getColoredCoinsCryptoCurrencies().contains(coloredCoinCurrency))
                        CurrencyUtil.addColoredCoinCryptoCurrency(coloredCoinCurrency);
                        listeners.stream().forEach(
                                e -> e.onNewColoredCoinAdded(coloredCoinCurrency)
                        );
                }
            }

            @Override
            public void onRemoved(ProtectedStorageEntry data) {
                if (data.getStoragePayload() instanceof ColoredCoinCurrency) {
                    ColoredCoinCurrency coloredCoinCurrency = (ColoredCoinCurrency) data.getStoragePayload();
                    log.debug("Colored coin removed message: " + coloredCoinCurrency);
                }

            }
        });
    }

    private ColoredCoinMetadata getAssetMetadataData(String assetId) throws ColoredCoinException {
        try {
            return coloredCoinsDataProvider.getColoredCoin(assetId);
        } catch (IOException | HttpException e) {
            throw new ColoredCoinException("Failed to get metadata for assetId: " + assetId, e);
        }
    }

    public void addOnNewColoredCoinListener(NewColoredCoinAddedListener listener) {
        listeners.add(listener);
    }

    public void addColoredCoin(ColoredCoinMetadata coloredCoinMetadata, String currencyCode) {
        ColoredCoinCurrency coloredCoin = new ColoredCoinCurrency(
                currencyCode + "-CC",
                coloredCoinMetadata.getName(),
                keyRing.getPubKeyRing());

        boolean result = p2PService.addData(coloredCoin, true);
        if (result) {
            log.trace("Add colored coins success. Colored coin: " + coloredCoin);
            listeners.stream().forEach(
                    e -> e.onNewColoredCoinAdded(coloredCoin)
            );
        } else {
            log.error("Failed to add colored coin");
        }
    }

    public void addColoredCoin(String assetId, String currencyCode) throws ColoredCoinException {
        ColoredCoinMetadata metadata = getAssetMetadataData(assetId);
        addColoredCoin(metadata, currencyCode);
    }
}
