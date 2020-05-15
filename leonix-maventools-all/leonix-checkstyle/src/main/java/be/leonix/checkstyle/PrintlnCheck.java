package be.leonix.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class PrintlnCheck extends AbstractCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[]{ TokenTypes.METHOD_CALL };
	}
	
	@Override
	public void visitToken(DetailAST methodCall) {
		DetailAST dot = methodCall.findFirstToken(TokenTypes.DOT);
		if (dot == null) return;
		
		DetailAST lastIdent = dot.getLastChild();
		if (lastIdent == null) return;
		
		if (lastIdent.getText().equals("println")) {
			DetailAST firstIdent = dot.getFirstChild();
			
			String text = FullIdent.createFullIdent(firstIdent).getText();
			if (text.equals("System.out") || text.equals("System.err")) {
				log(methodCall, "calls to println are not allowed, use a Logger for logging!");
			}
		}
	}
	
	@Override
	public int[] getAcceptableTokens() {
		return getDefaultTokens();
	}
	
	@Override
	public int[] getRequiredTokens() {
		return getDefaultTokens();
	}
}
