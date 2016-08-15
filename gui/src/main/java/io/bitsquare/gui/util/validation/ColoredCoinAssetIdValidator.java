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
package io.bitsquare.gui.util.validation;


import io.bitsquare.coloredcoins.providers.ColoredCoinMetadata;
import io.bitsquare.coloredcoins.providers.ColoredCoinsDataProvider;
import io.bitsquare.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class ColoredCoinAssetIdValidator extends InputValidator {
    private static final Logger log = LoggerFactory.getLogger(ColoredCoinAssetIdValidator.class);

    private final ColoredCoinsDataProvider coloredCoinsDataProvider;
    private ColoredCoinMetadata coloredCoinMetadata;

    @Inject
    public ColoredCoinAssetIdValidator(ColoredCoinsDataProvider coloredCoinsDataProvider) {
        this.coloredCoinsDataProvider = coloredCoinsDataProvider;
    }

    public ColoredCoinMetadata getColoredCoinMetadata() {
        return coloredCoinMetadata;
    }

    @Override
    public ValidationResult validate(String input) {
        try {
            coloredCoinMetadata = coloredCoinsDataProvider.getColoredCoin(input);
        } catch (HttpException | IOException e) {
            log.warn("Failed to get asset metadata: " + input, e);
            return new ValidationResult(false, "Failed to get asset metadata: " + input);
        }
        return new ValidationResult(true);
    }
}
