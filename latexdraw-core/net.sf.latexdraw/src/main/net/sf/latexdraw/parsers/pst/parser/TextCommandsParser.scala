package net.sf.latexdraw.parsers.pst.parser

import java.util.Optional

import net.sf.latexdraw.models.interfaces.shape.{Color, IShape}
import net.sf.latexdraw.view.latex.DviPsColors

/**
 * A parser grouping parsers parsing text commands.
 * This file is part of LaTeXDraw
 * Copyright (c) 2005-2017 Arnaud BLOUIN
 *  LaTeXDraw is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  LaTeXDraw is distributed without any warranty; without even the
 *  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE. See the GNU General Public License for more details.
 * 2012-10-14
 * @author Arnaud BLOUIN
 * @version 3.0
 */
trait TextCommandsParser extends PSTAbstractParser with PSTBracketBlockParser with IPSTCodeParser {
	/**
	 * Parses commands handling texts.
	 */
	def parsetextCommands(ctx:PSTContext) : Parser[List[IShape]] =
		parseUseFontCommand(ctx) | parseColorCommand(ctx) | parseTextcolorCommand(ctx) | parseTextSizeCommand(ctx) |
		parsetextCommandWithBlock(ctx) | parsetextCommandWithNoBlock(ctx) | parseFamilyFontCommand(ctx) |
		parseSerieFontCommand(ctx) | parseShapeFontCommand(ctx)



	/** Parses the font families. */
	private def parseShapeFontCommand(ctx:PSTContext):Parser[List[IShape]] =
		("\\upshape" | "\\itshape" | "\\slshape" | "\\scshape" | "\\it" | "\\sc" | "\\sl") ^^ {cmd =>
		ctx.textParsed +=cmd
		cmd match {
			case "\\upshape" => ctx.currFontShape = fontShape.normal
			case "\\itshape" => ctx.currFontShape = fontShape.italic
			case "\\it" => ctx.currFontShape = fontShape.italic
			case "\\slshape" => ctx.currFontShape = fontShape.slanted
			case "\\sl" => ctx.currFontShape = fontShape.slanted
			case "\\scshape" => ctx.currFontShape = fontShape.smallCaps
			case "\\sc" => ctx.currFontShape = fontShape.smallCaps
			case _ =>
		}
		Nil
	}

	/** Parses the font families. */
	private def parseSerieFontCommand(ctx:PSTContext):Parser[List[IShape]] = ("\\mdseries" | "\\bfseries" | "\\bf") ^^ {cmd =>
		ctx.textParsed +=cmd
		cmd match {
			case "\\mdseries" => ctx.currFontSerie = fontSerie.normal
			case "\\bfseries" => ctx.currFontSerie = fontSerie.bf
			case "\\bf" => ctx.currFontSerie = fontSerie.bf
			case _ =>
		}
		Nil
	}

	/** Parses the font families. */
	private def parseFamilyFontCommand(ctx:PSTContext):Parser[List[IShape]] = ("\\rmfamily" | "\\sffamily" | "\\ttfamily") ^^ {cmd =>
		ctx.textParsed +=cmd
		cmd match {
			case "\\rmfamily" => ctx.currFontFamily = fontFamily.rm
			case "\\sffamily" => ctx.currFontFamily = fontFamily.sf
			case "\\ttfamily" => ctx.currFontFamily = fontFamily.tt
			case _ =>
		}
		Nil
	}

	/** Parses the accent commands having no bracket block. */
	private def parsetextCommandWithNoBlock(ctx:PSTContext):Parser[List[IShape]] = "\\l" ^^ {cmd =>
		ctx.textParsed +=cmd
		ctx.parsedTxtNoTxt = false
		Nil
	}

	/** Parses the accent commands that may ba a bracket block. */
	private def parsetextCommandWithBlock(ctx:PSTContext):Parser[List[IShape]] =
	("\\`" | "\\'" | "\\^" | "\\\"" | "\\H" | "\\~" | "\\c" | "\\k" | "\\=" | "\\b" | "\\." | "\\d" |
		"\\r" | "\\u" | "\\v" | "\\t" | "\\textsf" | "\\textsc" | "\\textsl" | "\\underline" |
		"\\texttt" | "\\emph" | "\\textbf" | "\\textit") ~ opt(parseBracket(ctx)) ^^ {
		case cmd ~ txt =>
			ctx.textParsed +=cmd
			if(txt.isDefined) ctx.textParsed +="{"+txt.get+"}"
			ctx.parsedTxtNoTxt = false
			Nil
	}


	/** Parses the text size commands. */
	private def parseTextSizeCommand(ctx:PSTContext):Parser[List[IShape]] =
		("\\tiny" | "\\scriptsize" | "\\footnotesize" | "\\small" | "\\normalsize" | "\\large" | "\\Large" | "\\huge" | "\\Huge") ^^ {
		cmd =>
			ctx.textParsed +=cmd
			ctx.parsedTxtNoTxt = false
			Nil
	}


	/** Parses the command \textcolor */
	private def parseTextcolorCommand(ctx:PSTContext) : Parser[List[IShape]] = {
		val newCtx = new PSTContext(ctx)
		"\\textcolor" ~ parseColorBlock(newCtx) ~ parsePSTBlock(newCtx, newCtx.isPsCustom) ^^ {
			case _ ~ _ ~ shapes  => List(shapes)
		}
	}


	/** Parses the colour contained in the block. */
	private def parseColorBlock(ctx:PSTContext) : Parser[Unit] = {
		parseBracket(ctx) ^^ {
			colourTxt =>
				DviPsColors.INSTANCE.getColour(colourTxt) match {
  				case c:Optional[Color] => ctx.textColor = c.get
  				case _ =>
				}
		}
	}


	/** Parses the command \color */
	private def parseColorCommand(ctx:PSTContext) : Parser[List[IShape]] = "\\color" ~ parseColorBlock(ctx) ^^ {
		case _ ~ _ => Nil
	}


	/** Parses the usefont command. */
	private def parseUseFontCommand(ctx:PSTContext) : Parser[List[IShape]] =
		"\\usefont" ~ parseBracket(ctx) ~ parseBracket(ctx) ~ parseBracket(ctx) ~ parseBracket(ctx) ^^ {
		case _ ~ encoding ~ family ~ series ~ shapes =>
			ctx.textParsed += "\\usefont{"+encoding+"}{"+family+"}{"+series+"}{"+shapes+"}"
			fontShape.toFontShape(shapes) match {
				case Some(value) => ctx.currFontShape = value
				case _ =>
			}
			fontFamily.toFontFamily(family) match {
				case Some(value) => ctx.currFontFamily = value
				case _ =>
			}
			fontSerie.toFontSerie(series) match {
				case Some(value) => ctx.currFontSerie = value
				case _ =>
			}
			Nil
	}
}
