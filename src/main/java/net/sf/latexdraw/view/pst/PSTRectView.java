/*
 * This file is part of LaTeXDraw.
 * Copyright (c) 2005-2020 Arnaud BLOUIN
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package net.sf.latexdraw.view.pst;

import net.sf.latexdraw.model.api.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a PSTricks view of the LRect model.
 * @author Arnaud Blouin
 */
class PSTRectView extends PSTFrameView<Rectangle> {
	/**
	 * Creates and initialises a LRect PSTricks view.
	 * @param model The model to view.
	 */
	PSTRectView(final @NotNull Rectangle model) {
		super(model);
	}
}
