package fr.inria.sacha.spoon.diffSpoon;


import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import fr.labri.gumtree.actions.Action;
/**
 * Test Spoon Diff 
 * @author  Matias Martinez, matias.martinez@inria.fr
 *
 */
public class DiffSpoonTest {

	@Test
	public void testAnalyzeStringString() {
		String c1 = "" + "class X {" + "public void foo0() {" + " int x = 0;"
				+ "}" + "};";
		
		String c2 = "" + "class X {" + "public void foo1() {" + " int x = 0;"
				+ "}" + "};";
		
		
		DiffSpoon diff = new DiffSpoon(true);
		CtDiff editScript = diff.analyze(c1, c2);
		assertTrue(editScript.rootActions.size() == 1);
	}


	@Test
	public void exampleInsertAndUpdate() throws Exception{
		
		DiffSpoon diff = new DiffSpoon(true);
		File fl = new File(getClass().
				getResource("/examples/test1/TypeHandler1.java").getFile());
		File fr = new File(getClass().
				getResource("/examples/test1/TypeHandler2.java").getFile());
	
		CtDiff result = diff.analyze(fl,fr);
		List<Action> actions = result.getRootActions();
		assertEquals(actions.size(), 2);
		assertTrue(containsAction(actions, "INS", "CtInvocationImpl"));
		assertFalse(containsAction(actions, "DEL", "CtInvocationImpl"));
		assertFalse(containsAction(actions, "UPD", "CtInvocationImpl"));
		
		assertTrue(containsAction(actions, "UPD", "CtFieldAccessImpl"));
	}
	
	
	@Test
	public void exampleSingleUpdate() throws Exception{
		
		DiffSpoon diff = new DiffSpoon(true);
		File fl = new File(getClass().
				getResource("/examples/test2/CommandLine1.java").getFile());
		File fr = new File(getClass().
				getResource("/examples/test2/CommandLine2.java").getFile());
	
		CtDiff result = diff.analyze(fl,fr);
		List<Action> actions = result.getRootActions();
		assertEquals(actions.size(), 1);
		assertTrue(containsAction(actions, "UPD", "PAR-CtLiteralImpl"));
	
	}
	
	@Test
	public void exampleRemoveMethod() throws Exception{
		
		DiffSpoon diff = new DiffSpoon(true);
		File fl = new File(getClass().
				getResource("/examples/test3/CommandLine1.java").getFile());
		File fr = new File(getClass().
				getResource("/examples/test3/CommandLine2.java").getFile());
	
		CtDiff result = diff.analyze(fl,fr);
		List<Action> actions = result.getRootActions();
		assertEquals(actions.size(), 1);
		assertTrue(containsAction(actions, "DEL", "CtMethodImpl"));
	}
	
	
	@Test
	public void exampleInsert() throws Exception{
		
		DiffSpoon diff = new DiffSpoon(true);
		File fl = new File(getClass().
				getResource("/examples/test4/CommandLine1.java").getFile());
		File fr = new File(getClass().
				getResource("/examples/test4/CommandLine2.java").getFile());
	
		CtDiff result = diff.analyze(fl,fr);
		List<Action> actions = result.getRootActions();
		assertEquals(actions.size(), 1);
		assertTrue(containsAction(actions, "INS", "CtMethodImpl"));
	}
	
	@Test
	public void testMain() throws Exception{
		
		DiffSpoon diff = new DiffSpoon(true);
		File fl = new File(getClass().
				getResource("/examples/test4/CommandLine1.java").getFile());
		File fr = new File(getClass().
				getResource("/examples/test4/CommandLine2.java").getFile());
	
		DiffSpoon.main(new String []{fl.getAbsolutePath(), fr.getAbsolutePath()});
	}
	
	private boolean containsAction(List<Action> actions, String kindAction, String kindNode){
		//TODO: the kind of the action is not visible, To see in the next version of GumTree
		for (Action action : actions) {
			String toSt = action.toString();
			if(toSt.startsWith(kindAction)){
				return action.getNode().getTypeLabel().endsWith(kindNode);
			}
		}
		return false;
	}
}
