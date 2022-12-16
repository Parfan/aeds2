public class TP04Q07 {
	public static void main(String[] args) {
		int l, c, cases;
		String[] line;
		Matriz matriz1, matriz2;
		cases = MyIO.readInt();
		for (int i = 0; i < cases; i++) {
			l = MyIO.readInt();
			c = MyIO.readInt();
			matriz1 = new Matriz(l, c);
			for (int m = 0; m < l; m++) {
				line = MyIO.readLine().split(" ");
				for (int n = 0; n < c; n++) {
					matriz1.set(Integer.parseInt(line[n]), m, n);
				}
			}
			l = MyIO.readInt();
			c = MyIO.readInt();
			matriz2 = new Matriz(l, c);
			for (int m = 0; m < l; m++) {
				line = MyIO.readLine().split(" ");
				for (int n = 0; n < c; n++) {
					matriz2.set(Integer.parseInt(line[n]), m, n);
				}
			}
			matriz1.printPrimaryDiagonal();
			matriz1.printSecondaryDiagonal();
			matriz1.printSum(matriz2);
			matriz1.printMult(matriz2);
		}
	}
}

class No {
	No top, bot, esq, dir;
	int num;

	No () {
		this(0, null, null, null, null);
	}

	No (int num, No top, No bot, No esq, No dir) {
		this.num = num;
		this.top = top;
		this.bot = bot;
		this.esq = esq;
		this.dir = dir;
	}
}

class Matriz {
	int l, c;
	No first;

	Matriz(int l, int c) {
		this.l = l;
		this.c = c;
		first = new No();

		No tmp = first;
		for (int i = 0; i < l; i++) {
			if (i != 0) {
				tmp = new No(0, getNo(i-1, 0), null, null, null);
				tmp.top.bot = tmp;
			}

			for(int j = 1; j < c; j++) {
                tmp.dir = new No(0, null, null, tmp, null);
                tmp = tmp.dir;

                if(i != 0) {
                    tmp.top = getNo(i - 1, j);
                    tmp.top.bot = tmp;
                }
            }
		}
	}

	int get(int i, int j) {
		return getNo(i, j).num;
	}

	void set(int num, int i, int j) {
		No tmp = first;
		for (int m = 0; m < i; m++, tmp = tmp.bot);
		for (int m = 0; m < j; m++, tmp = tmp.dir);
		tmp.num = num;
	}
	
	No getNo(int i, int j) {
		if(i >= l || j >= c || i < 0 || j < 0) {
			return null;
		}
		No tmp = first;            
		for (int m = 0; m < i; m++, tmp = tmp.bot);
		for (int m = 0; m < j; m++, tmp = tmp.dir);
        return tmp;
	}

	void print() {
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < c; j++) {
				MyIO.print(get(i, j) + " ");
			}
			MyIO.println("");
		}
	}

	void printPrimaryDiagonal() {
		for (int i = 0; i < l; i++) {
			MyIO.print(get(i, i) + " ");
		}
		MyIO.println("");
    }
	
	void printSecondaryDiagonal() {
		for (int i = 0; i < l; i++) {
			MyIO.print(get(i, (i - c+1) * -1) + " ");
		}
		MyIO.println("");
	}

	void printSum(Matriz mat) {
		Matriz tmp = new Matriz(l, c);
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < c; j++) {
				tmp.set(get(i, j) + mat.get(i, j), i, j);
			}
		}
		tmp.print();
	}

	void printMult(Matriz mat) {
		Matriz tmp = new Matriz(l, c);
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < mat.l; j++) {
				int dot_product = 0;
				for (int k = 0; k < c; k++) {
					dot_product += get(i, k) * mat.get(k, j);
				}
				tmp.set(dot_product, i, j);
			}
		}
		tmp.print();
	}
}