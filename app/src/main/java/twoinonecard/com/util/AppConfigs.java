package twoinonecard.com.util;

import android.os.Environment;

public class AppConfigs {


	// ???
	//    public static float threshold = 75.0f;
	public static float threshold = 0.6f;
	public static boolean isInitSuccess = false;
	public static boolean isLandscape = true;
	public static String SWITCH_CAMERA="switch_camera";
	//??????????·??
	public static final String SOURCE_PATH = "/faceDiscern/faceSource";
	//???????????·??
	public static final String RECORD_PATH = "/faceDiscern/record";
	public static final String RECORD_PICTURE_PATH = "/faceDiscern/picture";
	//??????????·??
	public static final String MODEL_PATH = "/colorreco";

	public static int IMG_WIDTH = 512;
	public static int IMG_HEIGHT = 728;

	public static int MAX_DISCERN_NUM = 10;


	public static final String TAG = "FACE_DISCERN";

	public static final String ADD_ACTION = "com.sumeng.face.add_user_info";

	//me
	public static final int NETWORK_TYPE_WIFI = 1;
	public static final int NETWORK_TYPE_2G = 2;
	public static final int NETWORK_TYPE_3G = 3;

	/**
	 * ??????????·?? ?????????洢????
	 */
	public static final String BasePath= Environment.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * ??????????????
	 */
	private static final String DBDirectoryName = "wltlib";
	/**
	 * ???????????
	 */
	public static final String DBDirectoryNameL = "clog";

	/**
	 * ??????е?·??
	 */
	public static final String RootFile = BasePath+"/"+DBDirectoryName+"/";
	/**
	 * ??????е?·??
	 */
	public static final String RootFileL = BasePath+"/"+DBDirectoryNameL+"/";

	public static final String WITLIB = RootFile+"base.dat";

	public static final String LIC = RootFile+ "license.lic";

	public static String DIR_ROOT = "kdsm/Log";
	/** ??????????? */
	public static String FILE_NAME_EXTENSION_LOG = ".log";

	/**  UI?????????. */
	public static int uiWidth = 720;

	/**  UI?????????. */
	public static int uiHeight = 1080;

	/** The Constant CONNECTEXCEPTION. */
	public static final String CONNECTEXCEPTION = "????????????";

	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static final String UNKNOWNHOSTEXCEPTION = "????????????";

	/** The Constant SOCKETEXCEPTION. */
	public static final String SOCKETEXCEPTION = "?????????????????";

	/** The Constant SOCKETTIMEOUTEXCEPTION. */
	public static final String SOCKETTIMEOUTEXCEPTION = "??????????????";

	/** The Constant NULLPOINTEREXCEPTION. */
	public static final String NULLPOINTEREXCEPTION = "????????????????";

	/** The Constant NULLMESSAGEEXCEPTION. */
	public static final String NULLMESSAGEEXCEPTION = "??????????????";

	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static final String CLIENTPROTOCOLEXCEPTION = "Http???????????";

	/** ????????????. */
	public static final String MISSINGPARAMETERS = "??????а????????";

	/** The Constant REMOTESERVICEEXCEPTION. */
	public static final String REMOTESERVICEEXCEPTION = "????????????????";
	
}
