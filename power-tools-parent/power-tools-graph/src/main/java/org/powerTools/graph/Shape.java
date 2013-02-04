/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools.
 *
 *	The PowerTools are free software: you can redistribute them and/or
 *	modify them under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools are distributed in the hope that they will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.graph;


public enum Shape {
	DEFAULT				("<default>"),
	POLYGON				("polygon"),
	ELLIPSE				("ellipse"),
	OVAL				("oval"),
	CIRCLE				("circle"),
	POINT				("point"),
	EGG					("egg"),
	TRIANGLE			("triangle"),
	TEXT				("plaintext"),
	DIAMOND				("diamond"),
	TRAPEZIUM			("trapezium"),
	PARALLELOGRAM		("parallelogram"),
	HOUSE				("house"),
	PENTAGON			("pentagon"),
	HEXAGON				("hexagon"),
	SEPTAGON			("septagon"),
	OCTAGON				("octagon"),
	DOUBLE_CIRCLE		("doublecircle"),
	DOUBLE_OCTAGON		("doubleoctagon"),
	TRIPLE_OCTAGON		("tripleoctagon"),
	INVERTED_TRIANGLE	("invtriangle"),
	INVERTED_TRAPEZIUM	("invtrapezium"),
	INVERTED_HOUSE		("invhouse"),
	M_DIAMOND			("Mdiamond"),
	M_SQUARE			("Msquare"),
	M_CIRCLE			("Mcircle"),
	RECTANGLE			("rectangle"),
	SQUARE				("square"),
	CUBE				("box3d");
	
	private String text;
	
	private Shape (String text) {
		this.text = text;
	}
	
	@Override
	public String toString () {
		return this.text;
	}
}