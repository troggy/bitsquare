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

import io.bitsquare.app.Version;
import io.bitsquare.common.crypto.PubKeyRing;
import io.bitsquare.locale.CryptoCurrency;
import io.bitsquare.p2p.storage.payload.StoragePayload;

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

public final class ColoredCoinCurrency extends CryptoCurrency implements StoragePayload {

    private static final long serialVersionUID = Version.P2P_NETWORK_VERSION;

    public static final long TTL = TimeUnit.DAYS.toMillis(3650);

    private final PubKeyRing pubKeyRing;

    public ColoredCoinCurrency(String currencyCode, String name, PubKeyRing pubKeyRing) {
        super(currencyCode, name);
        this.pubKeyRing = pubKeyRing;
        this.isAsset = true;
    }

    @Override
    public PublicKey getOwnerPubKey() {
        return pubKeyRing.getSignaturePubKey();
    }

    @Override
    public long getTTL() {
        return TTL;
    }


}
