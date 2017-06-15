import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Params implements Closeable {
	public Scanner fileData;
	public String out = "file.bf";
	public String compiler = "gcc";
	public String template = "template/fs.bfc";
	public boolean noCompiler = false;
	public int size = 100;
	public int pos = 50;
	public boolean interpret = false;
	public boolean infinity = false;
	public boolean pbrain = false;

	public final static String HELP = "-f [file name]\tSelects a file\n"
			+ "-o [file name]\tSelects the name off the Output file\t\tDefault: file.bf\n"
			+ "-c [compiler]\tSelects the C Compiler\t\t\t\tDefault: gcc\n"
			+ "-s [size]\tDefines the Size of the Data Array\t\tDefault: 100\n"
			+ "-p [pos]\tDefines the Starting Position in the Data Array\tDefault: 50\n" + "--help\tShows this Screen";

	public Params(String[] args) {
		loop: for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-pbrain":
				pbrain = true;
				break;
			case "-t":
				template = args[++i];
				break;
			case "-i":
				interpret = true;
				break;
			case "-inf":
				infinity = true;
				template = "template/ds.bfc";
				break;
			case "-no-c":
			case "--no-compiler":
				noCompiler = true;
				break;
			case "--help":
				System.out.println(HELP);
				System.exit(0); // exits the Program
			case "-f":
			case "--file":
				try {
					fileData = new Scanner(new File(args[++i]));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit(1); // exits the Program with Error
				}
				break;
			case "-o":
			case "--out":
				out = args[++i];
				break;
			case "-c":
			case "compiler":
				compiler = args[++i];
				break;
			case "-s":
			case "--size":
				size = Integer.decode(args[++i]);
				break;
			case "-p":
			case "--pos":
				pos = Integer.decode(args[++i]);
				break;
			default:
				if (fileData == null) {
					StringBuilder build = new StringBuilder();
					for (; i < args.length; i++) {
						build.append(args[i]);
					}
					fileData = new Scanner(build.toString());

					break loop;
				}
				throw new RuntimeException("Unknown Param: " + args[i]);
			}
		}
		if (fileData == null) {
			fileData = new Scanner(System.in);
		}
	}

	@Override
	public void close() throws IOException {
		if (fileData != null) {
			fileData.close();
			fileData = null;
		}
	}
}
