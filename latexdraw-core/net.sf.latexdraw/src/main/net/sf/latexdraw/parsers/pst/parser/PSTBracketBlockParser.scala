package net.sf.latexdraw.parsers.pst.parser

import scala.util.parsing.input.CharArrayReader

/**
 * A parser that parses bracket block of PST commands.
 * This file is part of LaTeXDraw
 * Copyright (c) 2005-2017 Arnaud BLOUIN
 *  LaTeXDraw is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  LaTeXDraw is distributed without any warranty; without even the
 *  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE. See the GNU General Public License for more details.
 * 2012-05-02
 * @author Arnaud BLOUIN
 * @version 3.0
 */
trait PSTBracketBlockParser extends PSTAbstractParser {
	/**
	 * Parses brackets and their contents as text.
	 */
	def parseBracket(ctx : PSTContext, sep:String = "") : Parser[String] =
		"{" ~ rep1(chrExcept('}', CharArrayReader.EofCh)) ~ "}" ^^ { case _ ~ content ~ _ => content.mkString(sep) }


	/**
	 * Parses squared brackets and their contents as text.
	 */
	def parseSquaredBracket(ctx : PSTContext, sep:String = "") : Parser[String] =
		"[" ~ rep1(chrExcept(']', CharArrayReader.EofCh)) ~ "]" ^^ { case _ ~ content ~ _ => content.mkString(sep) }
}
