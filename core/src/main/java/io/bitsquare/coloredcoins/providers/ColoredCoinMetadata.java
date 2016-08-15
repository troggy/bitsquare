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

public class ColoredCoinMetadata {
    private String issuer;
    private String name;
    private String description;

    public ColoredCoinMetadata(String name, String description, String issuer) {
        this.issuer = issuer;
        this.name = name;
        this.description = description;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
