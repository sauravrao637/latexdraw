package net.sf.latexdraw.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class defines a filter for jpg files (*.jpg)
 * This file is part of LaTeXDraw
 * Copyright (c) 2005-2017 Arnaud BLOUIN
 *  LaTeXDraw is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  LaTeXDraw is distributed without any warranty; without even the
 *  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE. See the GNU General Public License for more details.
 * 05/24/10
 * @author Arnaud BLOUIN
 * @version 3.0
 */
public class JPGFilter extends FileFilter {
    /** The name of the extension of jpg file */
    public static final String JPG_EXTENSION = ".jpg";//$NON-NLS-1$

	@Override
	public boolean accept(final File file) {
		return file!=null && (file.getName().endsWith(JPG_EXTENSION)|| file.isDirectory());
	}


	@Override
	public String getDescription() {
		return "*" + JPG_EXTENSION; //$NON-NLS-1$
	}
}
