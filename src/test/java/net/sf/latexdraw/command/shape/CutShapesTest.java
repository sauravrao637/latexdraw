package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.List;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Shape;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command CutShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class CutShapesTest extends UndoableCmdTest<CutShapes> {
	List<Shape> copiedShapes;
	Drawing drawing;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			drawing = ShapeFactory.INST.createDrawing();
			final SelectShapes selectShapes = new SelectShapes(drawing);
			cmd = new CutShapes(selectShapes);
			copiedShapes = List.of(
				ShapeFactory.INST.createRectangle(),
				ShapeFactory.INST.createRectangle(),
				ShapeFactory.INST.createRectangle());
			copiedShapes.forEach(s -> drawing.addShape(s));
			selectShapes.addShape(copiedShapes.get(0));
			selectShapes.addShape(copiedShapes.get(2));
		});
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(() -> {
			final var drawing = Mockito.mock(Drawing.class);
			Mockito.when(drawing.getSelection()).thenReturn(ShapeFactory.INST.createGroup());
			cmd = new CutShapes(new SelectShapes(drawing));
		});
	}

	@Override
	protected Runnable doChecker() {
		return () -> {
			assertThat(drawing.size()).isEqualTo(1);
			assertThat(drawing.getShapeAt(0).orElseThrow()).isEqualTo(copiedShapes.get(1));
		};
	}

	@Override
	protected Runnable undoChecker() {
		return () -> {
			assertThat(drawing.size()).isEqualTo(3);
			assertThat(drawing.getShapes()).isEqualTo(copiedShapes);
		};
	}
}
