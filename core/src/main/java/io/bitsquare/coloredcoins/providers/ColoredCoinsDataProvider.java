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

package io.bitsquare.coloredcoins.providers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.bitsquare.app.BitsquareEnvironment;
import io.bitsquare.btc.BitcoinNetwork;
import io.bitsquare.btc.HttpClientProvider;
import io.bitsquare.http.HttpClient;
import io.bitsquare.http.HttpException;
import io.bitsquare.user.Preferences;

import javax.inject.Inject;
import java.io.IOException;

public class ColoredCoinsDataProvider extends HttpClientProvider {

    private static final String MAINNET_ENDPOINT = "http://api.coloredcoins.org:80/v3/";
    private static final String TESTNET_ENDPOINT = "http://testnet.api.coloredcoins.org:80/v3/";

    @Inject
    public ColoredCoinsDataProvider(HttpClient httpClient, Preferences preferences, BitsquareEnvironment environment) {
        super(httpClient, preferences, environment.getBitcoinNetwork() == BitcoinNetwork.MAINNET
                ? MAINNET_ENDPOINT
                : TESTNET_ENDPOINT);
    }

    private LinkedTreeMap<String, Object> getJson(String endpoint) throws IOException, HttpException {
        String response = httpClient.requestWithGET(endpoint);
        return new Gson().fromJson(response, LinkedTreeMap.class);
    }

    public ColoredCoinMetadata getColoredCoin(String assetId) throws IOException, HttpException {
        LinkedTreeMap<String, Object> treeMap = getJson("assetmetadata/" + assetId);
        String utxo = (String)treeMap.get("someUtxo");
        treeMap = getJson("assetmetadata/" + assetId + "/" + utxo);
        LinkedTreeMap<String, String> metadata = (LinkedTreeMap<String, String>)((LinkedTreeMap<String, Object>)treeMap.get("metadataOfIssuence")).get("data");
        return new ColoredCoinMetadata(metadata.get("assetName"), metadata.get("description"), metadata.get("issuer"));
    }
}
