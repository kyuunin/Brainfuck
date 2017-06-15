import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class DataArray implements IData {

	public DataArray(int size, int pos) {
		data = new char[size];
		ptr = pos;
	}

	char[] data;
	int ptr;
	Reader read = new InputStreamReader(System.in);

	@Override
	public void mvr() {
		ptr++;
	}

	@Override
	public void mvl() {
		ptr--;
	}

	@Override
	public void inc() {
		data[ptr]++;
	}

	@Override
	public void dec() {
		data[ptr]--;
	}

	@Override
	public void ptc() {
		System.out.print(data[ptr]);
	}

	@Override
	public void gtc() {
		try {
			data[ptr] = (char) read.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean check() {
		return data[ptr] == 0;
	}

}
