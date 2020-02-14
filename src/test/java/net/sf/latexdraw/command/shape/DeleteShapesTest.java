package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.List;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Shape;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command DeleteShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class DeleteShapesTest extends UndoableCmdTest<DeleteShapes> {
	List<Shape> shapes;
	Drawing drawing;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			drawing = ShapeFactory.INST.createDrawing();
			cmd = new DeleteShapes(drawing);
			shapes = List.of(
				ShapeFactory.INST.createRectangle(),
				ShapeFactory.INST.createRectangle(),
				ShapeFactory.INST.createRectangle());
			shapes.forEach(s -> drawing.addShape(s));
			cmd.setShape(shapes.get(0));
			cmd.addShape(shapes.get(2));
		});
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(() -> {
			drawing = ShapeFactory.INST.createDrawing();
			cmd = new DeleteShapes(drawing);
		});
	}

	@Override
	protected Runnable doChecker() {
		return () -> {
			assertThat(drawing.size()).isEqualTo(1);
			assertThat(drawing.getShapeAt(0).orElseThrow()).isEqualTo(shapes.get(1));
		};
	}

	@Override
	protected Runnable undoChecker() {
		return () -> {
			assertThat(drawing.size()).isEqualTo(3);
			assertThat(drawing.getShapes()).isEqualTo(shapes);
		};
	}

	@AfterEach
	void tearDownDeleteShapesTest() {
		this.shapes = null;
		this.drawing = null;
	}
}
