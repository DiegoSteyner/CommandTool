package commandtool.telaconfiguracao.dados.enums;

import java.util.function.Predicate;
import java.util.stream.Stream;

public enum AppsEnum 
{
	HANDBRAKE("HandBrakeCLI.exe", "HandBrakeCLI"),
	ZIP("", ""),
	MEDIA_INFO("MediaInfo.exe", "MkvMerge"),
	MKV_TOOLNIX("mkvmerge.exe", "MediaInfo")
	;
	
	String exeName;
	String appName;

	private AppsEnum(String exeName, String appName) {
		this.exeName = exeName;
		this.appName = appName;
	}

	public String getExeName() {
		return exeName;
	}

	public void setExeName(String exeName) {
		this.exeName = exeName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public static String[] getAllExeName()
	{
		return(Stream.of(values()).map(p -> p.exeName).filter(((Predicate<String>)String::isEmpty).negate()).toArray(String[]::new));
	}
}
