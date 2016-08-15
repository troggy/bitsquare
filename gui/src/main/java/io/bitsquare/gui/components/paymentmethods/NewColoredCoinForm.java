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

package io.bitsquare.gui.components.paymentmethods;

import io.bitsquare.coloredcoins.ColoredCoinException;
import io.bitsquare.coloredcoins.ColoredCoinsService;
import io.bitsquare.gui.components.InputTextField;
import io.bitsquare.gui.util.validation.ColoredCoinAssetIdValidator;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bitsquare.gui.util.FormBuilder.addButton;
import static io.bitsquare.gui.util.FormBuilder.addLabelInputTextField;

public class NewColoredCoinForm {
    private static final Logger log = LoggerFactory.getLogger(NewColoredCoinForm.class);

    protected final ColoredCoinsService coloredCoinsService;

    protected final GridPane gridPane;
    protected int gridRow;
    protected int gridRowFrom;
    private InputTextField assetIdTextField;
    private InputTextField currencyCodeTextField;
    private Button addColoredCoinButton;
    private final ColoredCoinAssetIdValidator coloredCoinAssetIdValidator;

    public NewColoredCoinForm(ColoredCoinsService coloredCoinsService, ColoredCoinAssetIdValidator coloredCoinAssetIdValidator, GridPane gridPane, int gridRow) {
        this.coloredCoinsService = coloredCoinsService;
        this.coloredCoinAssetIdValidator = coloredCoinAssetIdValidator;
        this.gridPane = gridPane;
        this.gridRow = gridRow;
    }

    public void init() {
        gridRowFrom = gridRow + 1;
        assetIdTextField = addLabelInputTextField(gridPane, ++gridRow, "Asset id:").second;
        currencyCodeTextField = addLabelInputTextField(gridPane, ++gridRow, "Currency code:").second;

        addColoredCoinButton = addButton(gridPane, ++gridRow, "Add new colored coin");

        addColoredCoinButton.setOnAction(e -> {
            if (coloredCoinAssetIdValidator.validate(assetIdTextField.getText()).isValid) {
                try {
                    coloredCoinsService.addColoredCoin(assetIdTextField.getText(), currencyCodeTextField.getText());
                } catch (ColoredCoinException e1) {
                    log.error("Failed to add colored coin", e1);
                }
            }
        });
    }

    public int getGridRow() {
        return gridRow;
    }

    public int getRowSpan() {
        return gridRow - gridRowFrom + 1;
    }
}
