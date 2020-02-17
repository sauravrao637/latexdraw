package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Group;
import net.sf.latexdraw.model.api.shape.Shape;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command MoveBackForegroundShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class MoveBackForegroundShapesTest extends UndoableCmdTest<MoveBackForegroundShapes> {
	boolean foreground;
	Drawing drawing;
	Group shape;
	Map<Shape, Integer> mementoIndexes;
	Shape s0;
	Shape s1;
	Shape s2;
	Shape s3;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			fixtureDrawing();
			foreground = true;
			shape.addShape(drawing.getShapeAt(0).orElseThrow());
			shape.addShape(drawing.getShapeAt(2).orElseThrow());
			cmd = new MoveBackForegroundShapes(shape, foreground, drawing);
		}, () -> {
			fixtureDrawing();
			foreground = false;
			shape.addShape(drawing.getShapeAt(2).orElseThrow());
			shape.addShape(drawing.getShapeAt(3).orElseThrow());
			cmd = new MoveBackForegroundShapes(shape, false, drawing);
		});
	}

	private void fixtureDrawing() {
		drawing = ShapeFactory.INST.createDrawing();
		s0 = ShapeFactory.INST.createEllipse();
		s1 = ShapeFactory.INST.createRectangle();
		s2 = ShapeFactory.INST.createRhombus();
		s3 = ShapeFactory.INST.createCircle();
		drawing.addShape(s0);
		drawing.addShape(s1);
		drawing.addShape(s2);
		drawing.addShape(s3);
		shape = ShapeFactory.INST.createGroup();
		mementoIndexes = drawing.getShapes().stream().collect(Collectors.toMap(sh -> sh, sh -> drawing.getShapes().indexOf(sh)));
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(() -> cmd = new MoveBackForegroundShapes(ShapeFactory.INST.createGroup(), true, ShapeFactory.INST.createDrawing()));
	}

	@Override
	protected Runnable doChecker() {
		return () -> {
			if(foreground) {
				assertThat(drawing.getShapeAt(0).orElseThrow()).isSameAs(s1);
				assertThat(drawing.getShapeAt(1).orElseThrow()).isSameAs(s3);
				assertThat(drawing.getShapeAt(2).orElseThrow()).isSameAs(s0);
				assertThat(drawing.getShapeAt(3).orElseThrow()).isSameAs(s2);
			}else {
				assertThat(drawing.getShapeAt(0).orElseThrow()).isSameAs(s2);
				assertThat(drawing.getShapeAt(1).orElseThrow()).isSameAs(s3);
				assertThat(drawing.getShapeAt(2).orElseThrow()).isSameAs(s0);
				assertThat(drawing.getShapeAt(3).orElseThrow()).isSameAs(s1);
			}
			assertThat(drawing.isModified()).isTrue();
		};
	}

	@Override
	protected Runnable undoChecker() {
		return () -> {
			assertThat(drawing.getShapes())
				.allSatisfy(sh -> assertThat(drawing.getShapes().indexOf(sh)).isEqualTo(mementoIndexes.get(sh)));
			assertThat(drawing.isModified()).isFalse();
		};
	}
}