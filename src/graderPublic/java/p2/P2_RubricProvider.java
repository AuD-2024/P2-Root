package p2;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class P2_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H02")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
