package commandtool.comum;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Predicate;

public class CommandToolFileVisitor implements FileVisitor<Path>
{
	private List<File> files;
	private Predicate<Path> condition;
	
	public CommandToolFileVisitor(List<File> files, Predicate<Path> condition) 
	{
		this.files = files;
		this.condition = condition;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException 
	{
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
	{
		if(condition.test(file))
		{
			files.add(file.toFile());
		}
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException 
	{
		return FileVisitResult.SKIP_SUBTREE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException 
	{
		return FileVisitResult.CONTINUE;
	}

}
