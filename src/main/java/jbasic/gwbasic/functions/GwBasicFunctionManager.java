package jbasic.gwbasic.functions;

import java.util.HashMap;
import java.util.Map;

import jbasic.common.functions.JBasicFunctionManager;
import jbasic.gwbasic.functions.conversion.AsciiFunction;
import jbasic.gwbasic.functions.conversion.CdblFunction;
import jbasic.gwbasic.functions.conversion.ChrFunction;
import jbasic.gwbasic.functions.conversion.CintFunction;
import jbasic.gwbasic.functions.conversion.CsngFunction;
import jbasic.gwbasic.functions.conversion.FixFunction;
import jbasic.gwbasic.functions.conversion.HexFunction;
import jbasic.gwbasic.functions.conversion.IntFunction;
import jbasic.gwbasic.functions.conversion.OctFunction;
import jbasic.gwbasic.functions.conversion.OrdFunction;
import jbasic.gwbasic.functions.conversion.StrFunction;
import jbasic.gwbasic.functions.conversion.ValFunction;
import jbasic.gwbasic.functions.math.AbsValFunction;
import jbasic.gwbasic.functions.math.ArcTanFunction;
import jbasic.gwbasic.functions.math.CosFunction;
import jbasic.gwbasic.functions.math.LogFunction;
import jbasic.gwbasic.functions.math.RndFunction;
import jbasic.gwbasic.functions.math.SignFunction;
import jbasic.gwbasic.functions.math.SineFunction;
import jbasic.gwbasic.functions.math.SqrtFunction;
import jbasic.gwbasic.functions.math.TanFunction;
import jbasic.gwbasic.functions.string.InstrFunction;
import jbasic.gwbasic.functions.string.LeftFunction;
import jbasic.gwbasic.functions.string.LenFunction;
import jbasic.gwbasic.functions.string.MidFunction;
import jbasic.gwbasic.functions.string.RightFunction;
import jbasic.gwbasic.functions.string.SpcFunction;
import jbasic.gwbasic.functions.string.StringFunction;
import jbasic.gwbasic.functions.string.TabFunction;
import jbasic.gwbasic.functions.system.EnvironFunction;
import jbasic.gwbasic.functions.system.EofFunction;
import jbasic.gwbasic.functions.system.FreeMemFunction;
import jbasic.gwbasic.functions.system.PeekFunction;
import jbasic.gwbasic.functions.system.UsrFunction;
import jbasic.gwbasic.functions.system.VarPtrFunction1;
import jbasic.gwbasic.functions.system.VarPtrFunction2;

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
	 * @see jbasic.common.functions.JBasicFunctionManager#getFunctionClasses()
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
