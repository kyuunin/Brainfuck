import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class DataList implements IData {
	Node node;
	Reader read = new InputStreamReader(System.in);

	@Override
	public void mvr() {
		node = node.moveRight();

	}

	@Override
	public void mvl() {
		node = node.moveLeft();

	}

	@Override
	public void inc() {
		node.c++;
	}

	@Override
	public void dec() {
		node.c--;
	}

	@Override
	public void ptc() {
		System.out.print(node.c);
	}

	@Override
	public void gtc() {
		try {
			node.c = (char) read.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static class Node {
		private Node right;
		private Node left;
		public char c;

		public Node moveLeft() {
			if (left == null) {
				left = new Node();
			}
			return left;
		}

		public Node moveRight() {
			if (right == null) {
				right = new Node();
			}
			return right;
		}

	}

	@Override
	public boolean check() {
		return node.c == 0;
	}
}
