package net.sf.latexdraw.command;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.sf.latexdraw.LatexdrawExtension;
import net.sf.latexdraw.service.LaTeXDataService;
import net.sf.latexdraw.service.PreferencesService;
import net.sf.latexdraw.view.latex.VerticalPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command ModifyLatexProperties. Generated by Interacto test-gen.
 */
@Tag("command")
@ExtendWith(LatexdrawExtension.class)
class ModifyLatexPropertiesTest extends UndoableCmdTest<ModifyLatexProperties> {
	Object value;
	Object oldValue;
	LatexProperties property;
	LaTeXDataService data;

	// The function that provides the memento, ie the values before setting the new value
	private Supplier<Object> mementoCmd;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Arrays.stream(LatexProperties.values()).map(prop -> () -> {
			data = new LaTeXDataService();
			property = prop;

			switch(property) {
				case PACKAGES -> {
					mementoCmd = () -> data.getPackages();
					value = "pkg";
				}
				case CAPTION -> {
					mementoCmd = () -> data.getCaption();
					value = "mycaption";
				}
				case LABEL -> {
					mementoCmd = () -> data.getLabel();
					value = "mylabel";
				}
				case POSITION_HORIZONTAL -> {
					mementoCmd = () -> data.isPositionHoriCentre();
					value = true;
				}
				case POSITION_VERTICAL -> {
					mementoCmd = () -> data.getPositionVertToken();
					value = VerticalPosition.FLOATS_PAGE;
				}
				case SCALE -> {
					mementoCmd = () -> data.getScale();
					value = 1.1d;
				}
				case COMMENT -> {
					mementoCmd = () -> data.getComment();
					value = "comment";
				}
			}

			oldValue = mementoCmd.get();
			cmd = new ModifyLatexProperties(data, property, value);
		});
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(() -> cmd = new ModifyLatexProperties(new LaTeXDataService(), LatexProperties.CAPTION, 10));
	}

	@Override
	protected Stream<Runnable> doCheckers() {
		return Stream.of(() -> {
			assertThat(value).isEqualTo(mementoCmd.get());
			assertThat(cmd.hadEffect()).isTrue();
		});
	}

	@Override
	protected Stream<Runnable> undoCheckers() {
		return Stream.of(() -> assertThat(oldValue).isEqualTo(mementoCmd.get()));
	}

	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	void testSetValue(final Runnable fixture) {
		fixture.run();
		cmd.setValue(null);
		assertThat(cmd.canDo()).isFalse();
	}
}
