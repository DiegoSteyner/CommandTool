package commandtool.comum.conditions;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

import commandtool.comum.utils.CommandToolsUtils;
import jutil.utils.StringUtils;

public class FileIsMedia implements Predicate<Path>{

	@Override
	public boolean test(Path f) 
	{
		File file = f.toFile();
		
		return file.getName() != null && StringUtils.endWithAny(file.getName().toLowerCase(), CommandToolsUtils.getExtensoes());
	}

}
