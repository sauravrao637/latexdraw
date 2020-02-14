package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Group;
import net.sf.latexdraw.model.api.shape.Point;
import net.sf.latexdraw.model.api.shape.Shape;
import net.sf.latexdraw.service.LaTeXDataService;
import net.sf.latexdraw.service.PreferencesService;
import net.sf.latexdraw.util.Tuple;
import net.sf.latexdraw.view.jfx.Canvas;
import net.sf.latexdraw.view.jfx.ViewFactory;
import net.sf.latexdraw.view.jfx.ViewShape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command DistributeShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class DistributeShapesTest extends UndoableCmdTest<DistributeShapes> {
	DistributeShapes.Distribution distribution;
	List<Point> oldPositions;
	List<ViewShape<?>> views;
	Canvas canvas;
	Group shape;
	List<Tuple<Point, Point>> memento;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(DistributeShapes.Distribution.values()).map(distrib -> () -> {
			canvas = Mockito.mock(Canvas.class);
			shape = ShapeFactory.INST.createGroup();
			shape.addShape(ShapeFactory.INST.createRectangle(ShapeFactory.INST.createPoint(10, 100), 50, 40));
			shape.addShape(ShapeFactory.INST.createRectangle(ShapeFactory.INST.createPoint(100, 300), 20, 100));
			shape.addShape(ShapeFactory.INST.createRectangle(ShapeFactory.INST.createPoint(250, -50), 200, 10));
			views = new ArrayList<>();

			final ViewFactory vfac = new ViewFactory(Mockito.mock(LaTeXDataService.class));
			IntStream.range(0, shape.size()).forEach(i -> {
				views.add(vfac.createView(shape.getShapeAt(i).orElseThrow()).get());
				Mockito.when(canvas.getViewFromShape(shape.getShapeAt(i).orElseThrow())).thenReturn(Optional.of(views.get(i)));
			});

			distribution = distrib;
			cmd = new DistributeShapes(canvas, distribution, shape);
			memento = shape.getShapes().stream().map(sh -> new Tuple<>(sh.getTopLeftPoint(), sh.getBottomRightPoint())).collect(Collectors.toList());
		});
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(() -> cmd = new DistributeShapes(Mockito.mock(Canvas.class),
			DistributeShapes.Distribution.VERT_EQ, ShapeFactory.INST.createGroup()));
	}

	@Override
	protected Runnable doChecker() {
		return () -> {
			switch(distribution) {
				case VERT_BOT:
					checkVertBase();
					assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint().getY()).isEqualTo(140);
					assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint().getY()).isEqualTo(180);
					break;
				case VERT_TOP:
					checkVertBase();
					assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint().getY()).isEqualTo(125);
					assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint().getY()).isEqualTo(165);
					break;
				case VERT_MID:
					checkVertBase();
					assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint().getY()).isEqualTo(132.5);
					assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint().getY()).isEqualTo(172.5);
					break;
				case VERT_EQ:
					checkVertBase();
					assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint().getY()).isEqualTo(110);
					assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint().getY()).isEqualTo(150);
					break;
				case HORIZ_LEFT:
					checkHorizBase();
					assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint().getX()).isEqualTo(130);
					assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint().getX()).isEqualTo(150);
					break;
				case HORIZ_RIGHT:
					checkHorizBase();
					assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint().getX()).isEqualTo(235);
					assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint().getX()).isEqualTo(255);
					break;
				case HORIZ_MID:
					checkHorizBase();
					assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint().getX()).isEqualTo(182.5);
					assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint().getX()).isEqualTo(202.5);
					break;
				case HORIZ_EQ:
					checkHorizBase();
					assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint().getX()).isEqualTo(145);
					assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint().getX()).isEqualTo(165);
					break;
			}
		};
	}

	private void checkVertBase() {
		assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint()).isEqualTo(memento.get(1).a);
		assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint()).isEqualTo(memento.get(1).b);
		assertThat(shape.getShapeAt(2).orElseThrow().getTopLeftPoint()).isEqualTo(memento.get(2).a);
		assertThat(shape.getShapeAt(2).orElseThrow().getBottomRightPoint()).isEqualTo(memento.get(2).b);
		assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint().getX()).isEqualTo(memento.get(0).a.getX());
		assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint().getX()).isEqualTo(memento.get(0).b.getX());
	}

	private void checkHorizBase() {
		assertThat(shape.getShapeAt(0).orElseThrow().getTopLeftPoint()).isEqualTo(memento.get(0).a);
		assertThat(shape.getShapeAt(0).orElseThrow().getBottomRightPoint()).isEqualTo(memento.get(0).b);
		assertThat(shape.getShapeAt(2).orElseThrow().getTopLeftPoint()).isEqualTo(memento.get(2).a);
		assertThat(shape.getShapeAt(2).orElseThrow().getBottomRightPoint()).isEqualTo(memento.get(2).b);
		assertThat(shape.getShapeAt(1).orElseThrow().getTopLeftPoint().getY()).isEqualTo(memento.get(1).a.getY());
		assertThat(shape.getShapeAt(1).orElseThrow().getBottomRightPoint().getY()).isEqualTo(memento.get(1).b.getY());
	}

	@Override
	protected Runnable undoChecker() {
		return () -> {
			int i = 0;
			for(final Shape shape : shape.getShapes()) {
				assertThat(shape.getTopLeftPoint()).isEqualTo(memento.get(i).a);
				assertThat(shape.getBottomRightPoint()).isEqualTo(memento.get(i).b);
				i++;
			}
		};
	}

	@AfterEach
	void tearDownDistributeShapesTest() {
		if(views != null) {
			views.forEach(v -> v.flush());
			views.clear();
		}
		if(shape != null) {
			shape.clear();
		}
		this.distribution = null;
		this.oldPositions = null;
		this.views = null;
		this.canvas = null;
		this.shape = null;
		this.memento = null;
	}
}
