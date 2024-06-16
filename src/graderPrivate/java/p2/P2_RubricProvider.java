package p2;

import org.sourcegrade.jagr.api.rubric.*;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import p2.binarytree.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class P2_RubricProvider implements RubricProvider {

    @SafeVarargs
    private static Criterion createCriterion(String shortDescription, int maxPoints, Callable<Method>... methodReferences) {
        return createCriterion(shortDescription, maxPoints, Arrays.stream(methodReferences).map(JUnitTestRef::ofMethod).toArray(JUnitTestRef[]::new));
    }

    private static Criterion createCriterion(String shortDescription, int maxPoints, JUnitTestRef... testRefs) {

        if (testRefs.length == 0) {
            return Criterion.builder()
                .shortDescription(shortDescription)
                .maxPoints(maxPoints)
                .build();
        }

        Grader.TestAwareBuilder graderBuilder = Grader.testAwareBuilder();

        for (JUnitTestRef testRef : testRefs) {
            graderBuilder.requirePass(testRef);
        }

        return Criterion.builder()
            .shortDescription(shortDescription)
            .grader(graderBuilder
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .maxPoints(maxPoints)
            .build();
    }

    private static Criterion createParentCriterion(String task, String shortDescription, Criterion... children) {
        return Criterion.builder()
            .shortDescription("H" + task + " | " + shortDescription)
            .addChildCriteria(children)
            .build();
    }

    public static final Criterion H1_1 = createCriterion("Die Methode [[[checkRule1]]] der Klasse [[[RBTreeRuleChecker]]] funktioniert vollständig korrekt", 1,
        () -> RBTreeCheckerTest.class.getMethod("testRule1", JsonParameterSet.class));

    public static final Criterion H1_2 = createCriterion("Die Methode [[[checkRule2]]] der Klasse [[[RBTreeRuleChecker]]] funktioniert vollständig korrekt", 1,
        () -> RBTreeCheckerTest.class.getMethod("testRule2", JsonParameterSet.class));

    public static final Criterion H1_3 = createCriterion("Die Methode [[[checkRule3]]] der Klasse [[[RBTreeRuleChecker]]] funktioniert vollständig korrekt", 1,
        () -> RBTreeCheckerTest.class.getMethod("testRule3", JsonParameterSet.class));

    public static final Criterion H1_4 = createCriterion("Die Methode [[[checkRule4]]] der Klasse [[[RBTreeRuleChecker]]] funktioniert vollständig korrekt", 2,
        () -> RBTreeCheckerTest.class.getMethod("testRule4", JsonParameterSet.class));

    public static final Criterion H1 = createParentCriterion("1", "Checker für RB Trees", H1_1, H1_2, H1_3, H1_4);

    public static final Criterion H2_1_1 = createCriterion("Die Methode [[[insert]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für einfache Fälle korrekt", 1,
        () -> InsertBSTTest.class.getMethod("testBSTInsertSimple", JsonParameterSet.class));

    public static final Criterion H2_1_2 = createCriterion("Die Methode [[[insert]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für komplexe Fälle korrekt", 1,
        () -> InsertBSTTest.class.getMethod("testBSTInsertComplex", JsonParameterSet.class));

    public static final Criterion H2_1 = createParentCriterion("2 a)", "BST Insert", H2_1_1, H2_1_2);

    public static final Criterion H2_2_1 = createCriterion("Die Methode [[[rotateLeft]]] der Klasse [[[RBTree]]] funktioniert vollständig korrekt", 1,
        () -> RotateTest.class.getMethod("testRotateLeft", JsonParameterSet.class));

    public static final Criterion H2_2_2 = createCriterion("Die Methode [[[rotateRight]]] der Klasse [[[RBTree]]] funktioniert vollständig korrekt", 1,
        () -> RotateTest.class.getMethod("testRotateRight", JsonParameterSet.class));

    public static final Criterion H2_2_3 = createCriterion("Die Methode [[[fixColorsAfterInsertion]]] der Klasse [[[RBTree]]] funktioniert korrekt, wenn nur die Farbe der Wurzel korrigiert wird", 1,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsRoot", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsRootOverride", JsonParameterSet.class))
        ));

    public static final Criterion H2_2_4 = createCriterion("Die Methode [[[fixColorsAfterInsertion]]] der Klasse [[[RBTree]]] funktioniert für einfache Fälle korrekt", 1,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsSimple", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsSimpleOverride", JsonParameterSet.class))
        ));

    public static final Criterion H2_2_5 = createCriterion("Die Methode [[[fixColorsAfterInsertion]]] der Klasse [[[RBTree]]] funktioniert für komplexe Fälle korrekt", 1,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsComplex", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> FixColorsTest.class.getMethod("testFixColorsComplexOverride", JsonParameterSet.class))
        ));

    public static final Criterion H2_2 = createParentCriterion("2 b)", "RB-Tree FixUp", H2_2_1, H2_2_2, H2_2_3, H2_2_4, H2_2_5);

    public static final Criterion H2_3_1 = createCriterion("Die Methode [[[insert]]] der Klasse [[[RBTree]]] funktioniert vollständig korrekt", 1,
        () -> InsertRBTreeTest.class.getMethod("testInsertRBTree"));

    public static final Criterion H2_3 = createParentCriterion("2 c)", "RB-Tree Insert", H2_3_1);

    public static final Criterion H2 = createParentCriterion("2", "RB-Tree Insert", H2_1, H2_2, H2_3);

    public static final Criterion H3_1_1 = createCriterion("Die Methode [[[inOrder]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für einfache Fälle korrekt", 1,
        () -> InOrderTest.class.getMethod("testInOrderSimple", JsonParameterSet.class));

    public static final Criterion H3_1_2 = createCriterion("Die Methode [[[inOrder]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für komplexe Fälle korrekt", 1,
        () -> InOrderTest.class.getMethod("testInOrderComplex", JsonParameterSet.class));

    public static final Criterion H3_1 = createParentCriterion("3 a)", "In Order", H3_1_1, H3_1_2);

    public static final Criterion H3_2_1 = createCriterion("Die Methode [[[findNext]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für einfache Fälle, welche bei dem Wurzelknoten starten, korrekt", 1,
        () -> FindNextTest.class.getMethod("testFindNextRootSimple", JsonParameterSet.class));

    public static final Criterion H3_2_2 = createCriterion("Die Methode [[[findNext]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für einfache Fälle korrekt", 1,
        () -> FindNextTest.class.getMethod("testFindNextSimple", JsonParameterSet.class));

    public static final Criterion H3_2_3 = createCriterion("Die Methode [[[findNext]]] der Klasse [[[AbstractBinarySearchTree]]] funktioniert für komplexe Fälle korrekt", 1,
        () -> FindNextTest.class.getMethod("testFindNextComplex", JsonParameterSet.class));

    public static final Criterion H3_2 = createParentCriterion("3 b)", "Find Next", H3_2_1, H3_2_2, H3_2_3);

    public static final Criterion H3_3_1 = createCriterion("Die Methode [[[prefixSearch]]] der Klasse [[[AutoComplete]]] funktioniert für einfache Fälle korrekt", 1,
        () -> PrefixSearchTest.class.getMethod("testPrefixSearchSimple", JsonParameterSet.class));

    public static final Criterion H3_3_2 = createCriterion("Die Methode [[[prefixSearch]]] der Klasse [[[AutoComplete]]] funktioniert für komplexe Fälle korrekt", 1,
        () -> PrefixSearchTest.class.getMethod("testPrefixSearchComplex", JsonParameterSet.class));

    public static final Criterion H3_3 = createParentCriterion("3 c)", "Prefix Search", H3_3_1, H3_3_2);

    public static final Criterion H3 = createParentCriterion("3", "Autovervollständigung", H3_1, H3_2, H3_3);

    public static final Criterion H4_1_1 = createCriterion("Die Methode [[[blackHeight]]] der Klasse [[[RBTree]]] funktioniert vollständig korrekt", 1,
        () -> BlackHeightTest.class.getMethod("testBlackHeight", JsonParameterSet.class));

    public static final Criterion H4_1 = createParentCriterion("4 a)", "Black Height", H4_1_1);

    public static final Criterion H4_2_1 = createCriterion("Die Methode [[[findBlackNodeWithBlackHeight]]] der Klasse [[[RBTree]]] funktioniert für einfache Fälle, welche nach dem kleinsten Knoten suchen, korrekt", 1,
        () -> FindBlackNodeTest.class.getMethod("testFindSmallestBlackNodeSimple", JsonParameterSet.class));

    public static final Criterion H4_2_2 = createCriterion("Die Methode [[[findBlackNodeWithBlackHeight]]] der Klasse [[[RBTree]]] funktioniert für komplexe Fälle, welche nach dem kleinsten Knoten suchen, korrekt", 1,
        () -> FindBlackNodeTest.class.getMethod("testFindSmallestBlackNodeComplex", JsonParameterSet.class));

    public static final Criterion H4_2_3 = createCriterion("Die Methode [[[findBlackNodeWithBlackHeight]]] der Klasse [[[RBTree]]] funktioniert für Fälle, welche nach dem größten Knoten suchen, korrekt", 1,
        () -> FindBlackNodeTest.class.getMethod("testFindGreatestBlackNode", JsonParameterSet.class));

    public static final Criterion H4_2 = createParentCriterion("4 b)", "Find Black Node", H4_2_1, H4_2_2, H4_2_3);

    public static final Criterion H4_3_1 = createCriterion("Die Methode [[[join]]] der Klasse [[[RBTree]]] funktioniert korrekt, wenn beide Bäume die selbe Schwarzhöhe besitzten", 1,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinSameHeight", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinSameHeightOverride", JsonParameterSet.class))
        ));

    public static final Criterion H4_3_2 = createCriterion("Die Methode [[[join]]] der Klasse [[[RBTree]]] funktioniert korrekt, wenn der rechte Baum eine größere Schwarzhöhe besitzt", 2,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinRightGreater", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinRightGreaterOverride", JsonParameterSet.class))
        ));

    public static final Criterion H4_3_3 = createCriterion("Die Methode [[[join]]] der Klasse [[[RBTree]]] funktioniert korrekt, wenn der linke Baum eine größere Schwarzhöhe besitzt", 2,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinLeftGreater", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinLeftGreaterOverride", JsonParameterSet.class))
        ));

    public static final Criterion H4_3_4 = createCriterion("Die Methode [[[join]]] der Klasse [[[RBTree]]] funktioniert vollständig korrekt", 1,
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinSameHeight", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinSameHeightOverride", JsonParameterSet.class))
        ),
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinRightGreater", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinRightGreaterOverride", JsonParameterSet.class))
        ),
        JUnitTestRef.or(
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinLeftGreater", JsonParameterSet.class)),
            JUnitTestRef.ofMethod(() -> JoinTest.class.getMethod("testJoinLeftGreaterOverride", JsonParameterSet.class))
        ));

    public static final Criterion H4_3 = createParentCriterion("4 c)", "Joinen von RB-Trees", H4_3_1, H4_3_2, H4_3_3, H4_3_4);

    public static final Criterion H4 = createParentCriterion("4", "Joinen von RB-Trees", H4_1, H4_2, H4_3);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P2")
        .addChildCriteria(H1, H2, H3, H4)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

}
