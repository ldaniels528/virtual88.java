package org.ldaniels528.javapc.jbasic.gwbasic.functions;

import java.util.HashMap;
import java.util.Map;

import org.ldaniels528.javapc.jbasic.common.functions.JBasicFunctionManager;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.AsciiFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.CdblFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.ChrFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.CintFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.CsngFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.FixFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.HexFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.IntFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.OctFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.OrdFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.StrFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.conversion.ValFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.AbsValFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.ArcTanFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.CosFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.LogFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.RndFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.SignFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.SineFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.SqrtFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.math.TanFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.InstrFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.LeftFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.LenFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.MidFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.RightFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.SpcFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.StringFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.string.TabFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.EnvironFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.EofFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.FreeMemFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.PeekFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.UsrFunction;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.VarPtrFunction1;
import org.ldaniels528.javapc.jbasic.gwbasic.functions.system.VarPtrFunction2;

/**
 * GWBASIC Function Manager
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicFunctionManager extends JBasicFunctionManager {
	
	/**
	 * Default Constructor
	 */
	public GwBasicFunctionManager() {
		super();
	}
  
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.common.functions.JBasicFunctionManager#getFunctionClasses()
	 */
	protected Map<String,Class> getFunctionClasses() {	  
		final Map<String,Class> functions = new HashMap<String,Class>();
		functions.put( "ABS", AbsValFunction.class );
		functions.put( "ASC", AsciiFunction.class );
		functions.put( "ATN", ArcTanFunction.class );
		functions.put( "CHR$", ChrFunction.class );
		functions.put( "CDBL", CdblFunction.class );
		functions.put( "CINT", CintFunction.class );
		functions.put( "COS", CosFunction.class );
		functions.put( "CSNG", CsngFunction.class );
		functions.put( "ENVIRON$", EnvironFunction.class );
		functions.put( "EOF", EofFunction.class );
		functions.put( "FIX", FixFunction.class );
		functions.put( "FRE", FreeMemFunction.class );
		functions.put( "HEX$", HexFunction.class );
		functions.put( "INPUT$", InputFunction.class );
		functions.put( "INSTR", InstrFunction.class );
		functions.put( "INT", IntFunction.class );
		functions.put( "LEFT$", LeftFunction.class );
		functions.put( "LEN", LenFunction.class );
		functions.put( "LOG", LogFunction.class );
		functions.put( "MID$", MidFunction.class );
		functions.put( "OCT$", OctFunction.class );
		functions.put( "ORD", OrdFunction.class );
		functions.put( "PEEK", PeekFunction.class );
		functions.put( "PMAP", PMapFunction.class );
		functions.put( "POS", PosFunction.class );
		functions.put( "RIGHT$", RightFunction.class );
		functions.put( "RND", RndFunction.class );
		functions.put( "SCREEN", ScreenFunction.class );
		functions.put( "SGN", SignFunction.class );
		functions.put( "SIN", SineFunction.class );
		functions.put( "SPACE$", SpcFunction.class );
		functions.put( "SPC", SpcFunction.class );
		functions.put( "SQR", SqrtFunction.class );
		functions.put( "STR$", StrFunction.class );
		functions.put( "STRING$", StringFunction.class );
		functions.put( "TAB", TabFunction.class );
		functions.put( "TAN", TanFunction.class );
		functions.put( "USR", UsrFunction.class );
		functions.put( "VAL", ValFunction.class );
		functions.put( "VARPTR", VarPtrFunction1.class );
		functions.put( "VARPTR$", VarPtrFunction2.class );
		return functions;
	}

}
