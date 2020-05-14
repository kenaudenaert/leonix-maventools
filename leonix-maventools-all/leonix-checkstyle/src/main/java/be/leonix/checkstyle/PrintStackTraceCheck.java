package be.landc.checkstyle;

import java.util.ArrayList;
import java.util.List;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class PrintStackTraceCheck extends Check {

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
		
		if (lastIdent.getText().equals("printStackTrace")) {
			DetailAST elist = methodCall.findFirstToken(TokenTypes.ELIST);
			boolean parametersFound = (elist != null && !findChildASTsOfType(elist, TokenTypes.IDENT).isEmpty());
			if (! parametersFound) {
				log(methodCall, "calls to printStackTrace are not allowed, use a Logger for exception logging!");
			}
		}
	}
	
	private static List<DetailAST> findChildASTsOfType(DetailAST parent, int type) {
		List<DetailAST> children = new ArrayList<DetailAST>();
		
		DetailAST child = parent.getFirstChild();
		while (child != null) {
			if (child.getType() == type)
				children.add(child);
			else {
				children.addAll(findChildASTsOfType(child, type));
			}
			child = child.getNextSibling();
		}
		return children;
	}
}
