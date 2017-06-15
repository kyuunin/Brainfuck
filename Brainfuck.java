import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Brainfuck {

	public static void main(String[] args) throws IOException {
		try (Params p = new Params(args)) {
			if (p.interpret) {
				if (p.infinity)
					interpret(p, new DataList());
				else
					interpret(p, new DataArray(p.size, p.pos));
			} else {
				toC(p);
			}
		}
	}

	public static void info(String s, Object... o) {
		Console c = System.console();
		if (c != null) {
			c.printf(s, o);
		}
	}

	public static void pipe(String s, Object... o) {
		Console c = System.console();
		if (c == null) {
			System.out.printf(s, o);
		}
	}

	public static void toC(Params param) {
		info("[Brainfuck] start compiling.%n");
		String code = loadTemplate(param);
		pipe(code);

		if (!param.noCompiler) {
			compileC(param, code);
		}
	}

	public static String loadTemplate(Params param) {
		try (Scanner scan = new Scanner(new File(param.template))) {
			String code;
			String proce;
			if (param.pbrain) {
				Map<String, StringBuilder> builds = passembly(param.fileData);
				code = builds.remove("main").toString();
				proce = toPString(builds);
			} else {
				code = assembly(param.fileData);
				proce = "";
			}
			StringBuilder build = new StringBuilder();
			while (scan.hasNext()) {
				String s = scan.nextLine();
				if (s.contains("#brainfuck")) {
					s = s.replaceFirst("#brainfuck SIZE", "#define SIZE " + param.size);
					s = s.replaceFirst("#brainfuck POS", "#define POS " + param.pos);
					s = s.replaceFirst("#brainfuck CODE", code);
					s = s.replaceFirst("#brainfuck PROCE", proce);
					s = s.replaceFirst("#brainfuck PBRAIN", "#define PBRAIN " + (param.pbrain ? 1 : 0));
				}
				build.append(s).append('\n');
			}
			return build.toString();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static String assembly(Scanner fileData) {
		StringBuilder build = new StringBuilder();
		String buffer;
		while (fileData.hasNext()) {
			buffer = fileData.nextLine();
			for (char c : buffer.toCharArray()) {
				switch (c) {
				case '>':
					build.append("MVR ");
					break;
				case '<':
					build.append("MVL ");
					break;
				case '+':
					build.append("INC ");
					break;
				case '-':
					build.append("DEC ");
					break;
				case '.':
					build.append("PTC ");
					break;
				case ',':
					build.append("GTC ");
					break;
				case '[':
					build.append("SOL ");
					break;
				case ']':
					build.append("EOL ");
					break;
				}
			}
		}
		return build.toString();
	}

	private static class Tuple<A, B> {
		A a;
		B b;

		public Tuple(A a, B b) {
			super();
			this.a = a;
			this.b = b;
		}
	}

	public static Map<String, StringBuilder> passembly(Scanner fileData) {

		Random rand = new Random();
		Map<String, StringBuilder> builders = new HashMap<>();
		Deque<Tuple<String, StringBuilder>> buildStack = new LinkedList<>();
		StringBuilder build = new StringBuilder();
		String name = "main";

		builders.put(name, build);

		String buffer;
		while (fileData.hasNext()) {
			buffer = fileData.nextLine();
			for (char c : buffer.toCharArray()) {
				switch (c) {
				case '>':
					build.append("MVR ");
					break;
				case '<':
					build.append("MVL ");
					break;
				case '+':
					build.append("INC ");
					break;
				case '-':
					build.append("DEC ");
					break;
				case '.':
					build.append("PTC ");
					break;
				case ',':
					build.append("GTC ");
					break;
				case '[':
					build.append("SOL ");
					break;
				case ']':
					build.append("EOL ");
					break;
				case ':':
					build.append("RUN ");
					break;
				case '(':
					buildStack.push(new Tuple<>(name, build));
					
					do {
						name = String.format("fx%X", rand.nextLong());
					} while (builders.containsKey(name));
					
					build.append(String.format("DEF(%s) ", name));
					build = new StringBuilder();
					builders.put(name, build);
					break;
				case ')':
					Tuple<String, StringBuilder> tmp = buildStack.pop();
					name = tmp.a;
					build = tmp.b;
					break;
				}
			}
		}
		return builders;
	}

	public static String toPString(Map<String, StringBuilder> builds) {
		StringBuilder b = new StringBuilder();
		builds.forEach((name, body) -> {
			b.append(String.format("SOP(%s) ", name));
			b.append(body);
			b.append("EOP ");
		});
		return b.toString();
	}

	public static void compileC(Params param, String build) {
		try (PrintStream out = new PrintStream(new FileOutputStream("~tmp.c"))) {
			out.println(build);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		info("[Brainfuck] created ~tmp.c%n");
		info("[Brainfuck] starting %s%n", param.compiler);
		try {

			Process p = Runtime.getRuntime().exec(String.format("%s -o %s ~tmp.c", param.compiler, param.out));

			Scanner in = new Scanner(p.getInputStream());
			if (in.hasNext()) {
				info("[%s] Std Out:%n", param.compiler);
				while (in.hasNext()) {
					info(in.nextLine());
				}
			}
			in.close();

			Scanner err = new Scanner(p.getErrorStream());
			if (err.hasNext()) {
				System.out.printf("[%s] Std Err:%n", param.compiler);
				while (err.hasNext()) {
					System.err.println(err.nextLine());
				}
			}
			err.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			info("[Brainfuck] delete ~tmp.c");
			new File("~tmp.c").delete();
		}
	}

	public static void interpret(Params param, IData data) {
		Deque<Integer> stack = new LinkedList<>();
		String buffer = "";

		while (param.fileData.hasNext()) {
			buffer += param.fileData.nextLine();
		}

		char[] chars = trim(buffer).toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '>':
				data.mvl();
				break;
			case '<':
				data.mvr();
				break;
			case '+':
				data.inc();
				break;
			case '-':
				data.dec();
				break;
			case '.':
				data.ptc();
				break;

			case ',':
				data.gtc();
				break;
			case '[':
				if (data.check()) {
					int count = 1;
					for (i++; 0 < count/* && i < chars.length */; i++) {
						switch (chars[i]) {
						case '[':
							count++;
							break;
						case ']':
							count--;
							break;

						default:
							break;
						}
					}
					i--;
				} else {
					stack.push(i);
				}
				break;
			case ']':
				i = stack.pop() - 1;
				break;

			}

		}
	}

	public static String trim(String s) {
		return s.replaceAll("[^+-.,\\[\\]<>]", "");
	}
}
