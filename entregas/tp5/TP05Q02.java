import java.util.Date;
import java.util.Locale;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TP05Q02 {
    public static void main(String[] args) throws Exception {
		String[] entradas = new String[1000];
		int numEntrada = 0;
        
        Scanner sc = new Scanner(System.in);
		do {
            entradas[numEntrada] = sc.nextLine();
		} while (isFim(entradas[numEntrada++]) == false);
		numEntrada--;

        if (numEntrada == 0) {
			sc.close();
            return;
		}

        Game game = new Game();
        ArvoreArvore gameAA = new ArvoreArvore();
        ArrayList<String> fileLines = readFile("/tmp/games.csv");
        for (int i = 0; i < numEntrada; i++) {
            for (String line : fileLines) {
                if (entradas[i].equals(line.split(",")[0])) {
                    game = new Game();
                    game.ler(line);
					try {
						gameAA.inserir(game);
					} catch (Exception e) {
					}
                    break;
                }
            }
        }

		numEntrada = sc.nextInt();
		String[] comando_game = sc.nextLine().split(" ");
        for (int i = 0; i < numEntrada; i++) {
            if (comando_game[0].equals("I")) {
				for (String line : fileLines) {
					if (comando_game[comando_game.length - 1].equals(line.split(",")[0])) {
						game = new Game();
                        game.ler(line);
                        break;
                    }
                }
				try { gameAA.inserir(game); } catch (Exception e) {}
            }
			comando_game = sc.nextLine().split(" ");
        }
		
        String entrada;
		while (isFim(entrada = sc.nextLine()) == false) {
			gameAA.pesquisar(entrada);
		}
		sc.close();
    }

    public static boolean isFim(String s) {
        return s.equals("FIM");
    }

    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            FileReader arq = new FileReader(fileName);
            BufferedReader br = new BufferedReader(arq);

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                lines.add(line);
            }

            br.close();
        } catch (IOException ioe) {
        }
        
        return lines;
    }
}

class No {
    public char elemento;
    public No esq;
    public No dir;
    public No2 outro;

    No(char elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
        this.outro = null;
    }

    No(char elemento, No esq, No dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
        this.outro = null;
    }
}

class No2 {
    public Game elemento;
    public No2 esq;
    public No2 dir;

    No2(Game elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
    }

    No2(Game elemento, No2 esq, No2 dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreArvore {
    private No raiz;

    public ArvoreArvore() throws Exception {
        raiz = null;
        char[] caracteres = {
            'D', 'R', 'Z', 'X',
            'V', 'B', 'F', 'P',
            'U', 'I', 'G', 'E',
            'J', 'L', 'H', 'T',
            'A', 'W', 'S', 'O',
            'M', 'N', 'K', 'C',
            'Y', 'Q'
        };
        for (char c : caracteres) {
            inserir(c);
        }
    }

    public void inserir(char letra) throws Exception {
        raiz = inserir(letra, raiz);
    }

    public void inserir(Game game) throws Exception {
        inserir(game, raiz);
    }

    private No inserir(char letra, No i) throws Exception {
        if (i == null) {
            i = new No(letra);
        } else if (letra < i.elemento) {
            i.esq = inserir(letra, i.esq);
        } else if (letra > i.elemento) {
            i.dir = inserir(letra, i.dir);
        } else {
            throw new Exception("Erro ao inserir!");
        }
        return i;
    }

    public void inserir(Game game, No i) throws Exception {
        if (i == null) {
            throw new Exception("Erro ao inserir: caractere invalido!");
        } else if (game.getName().charAt(0) < i.elemento) {
            inserir(game, i.esq);
        } else if (game.getName().charAt(0) > i.elemento) {
            inserir(game, i.dir);
        } else {
            i.outro = inserir(game, i.outro);
        }
    }

    private No2 inserir(Game game, No2 i) throws Exception {
        if (i == null) {
            i = new No2(game);
        } else if (game.getName().compareTo(i.elemento.getName()) < 0) {
            i.esq = inserir(game, i.esq);
        } else if (game.getName().compareTo(i.elemento.getName()) > 0) {
            i.dir = inserir(game, i.dir);
        } else {
            throw new Exception("Erro ao inserir: elemento existente!");
        }
        return i;
    }

    public void mostrar() {
        mostrar(raiz);
    }

    public void mostrar(No i) {
        if (i != null) {
            mostrar(i.esq);
            // System.out.println("Letra: " + i.elemento);
            mostrar(i.outro);
            mostrar(i.dir);
        }
    }

    public void mostrar(No2 i) {
        if (i != null) {
            mostrar(i.esq);
            // System.out.println(i.elemento);
            mostrar(i.dir);
        }
    }

    public void pesquisar(String str) {
        MyIO.println(str);
        MyIO.print("raiz ");
        if (pesquisar(raiz, str))
            MyIO.println(" SIM");
        else
            MyIO.println(" NAO");
    }

    private boolean pesquisar(No no, String str) {
        boolean resp;
        if (no == null) {
            resp = false;
        } else {
            if ((resp = pesquisarSegundaArvore(no.outro, str)) == false) {
                MyIO.print(" ESQ ");
                resp = pesquisar(no.esq, str);
            }
            if (resp == false) {
                MyIO.print(" DIR ");
                resp = pesquisar(no.dir, str);
            }
        }
        return resp;
    }

    private boolean pesquisarSegundaArvore(No2 no, String str) {
        boolean resp;
        if (no == null) {
            resp = false;
        } else if (str.compareTo(no.elemento.getName()) < 0) {
            MyIO.print("esq ");
            resp = pesquisarSegundaArvore(no.esq, str);
        } else if (str.compareTo(no.elemento.getName()) > 0) {
            MyIO.print("dir ");
            resp = pesquisarSegundaArvore(no.dir, str);
        } else {
            resp = true;
        }
        return resp;
    }
}

class Game {
	private int app_id;
	private String name;
	private Date release_date;
	private String owners;
	private int age;
	private float price;
	private int dlcs;
	private String[] languages;
	private String website;
	private boolean windows;
	private boolean mac;
	private boolean linux;
	private float upvotes;
	private int avg_pt;
	private String developers;
	private String[] genres;

	public Game() {
		this.app_id = -1;
		this.name = "";
		this.release_date = new Date();
		this.owners = "";
		this.age = 0;
		this.price = 0.0f;
		this.dlcs = 0;
		this.languages = new String[0];
		this.website = "";
		this.windows = false;
		this.mac = false;
		this.linux = false;
		this.upvotes = 0.0f;
		this.avg_pt = 0;
		this.developers = "";
		this.genres = new String[0];
	}

	public Game(
		int app_id, String name, Date release_date, String owners,
		int age, float price, int dlcs, String[] languages,
		String website, boolean windows, boolean mac, boolean linux,
		float upvotes, int avg_pt, String developers, String[] genres
	) {
		this.app_id = app_id;
		this.name = name;
		this.release_date = release_date;
		this.owners = owners;
		this.age = age;
		this.price = price;
		this.dlcs = dlcs;
		this.languages = languages;
		this.website = website;
		this.windows = windows;
		this.mac = mac;
		this.linux = linux;
		this.upvotes = upvotes;
		this.avg_pt = avg_pt;
		this.developers = developers;
		this.genres = genres;
	}

    public void ler(String rawLine) {
        String[] lineArray = new String[17];
        int charAfterCommaIndex, lineArrayIndex;
        charAfterCommaIndex = lineArrayIndex = 0;

        for (int i = 0; i < rawLine.length(); i++) {
            if (rawLine.charAt(i) == '"') {
                charAfterCommaIndex = ++i;
                do { } while (rawLine.charAt(i++) != '"');
                lineArray[lineArrayIndex] = rawLine.substring(charAfterCommaIndex, i - 1);
                charAfterCommaIndex = i + 1;
                lineArrayIndex++;
            } else if (rawLine.charAt(i) == ',') {
                lineArray[lineArrayIndex] = rawLine.substring(charAfterCommaIndex, i);
                charAfterCommaIndex = i + 1;
                lineArrayIndex++;
            } else if (i == rawLine.length() - 1) {
                lineArray[lineArrayIndex] = rawLine.substring(charAfterCommaIndex, i + 1);
            }
        }

        this.app_id = Integer.parseInt(lineArray[0]);
        this.name = lineArray[1];
        try {
            this.release_date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(lineArray[2]);
        } catch (ParseException e) {
        }
        this.owners = lineArray[3];
        this.age = Integer.parseInt(lineArray[4]);
        this.price = Float.parseFloat(lineArray[5]);
        this.dlcs = Integer.parseInt(lineArray[6]);
        this.languages = this.stringToStringArray(lineArray[7]);
        this.website = lineArray[8];
        this.windows = Boolean.parseBoolean(lineArray[9]);
        this.mac = Boolean.parseBoolean(lineArray[10]);
        this.linux = Boolean.parseBoolean(lineArray[11]);
        this.upvotes = Float.parseFloat(lineArray[12])/(
            Float.parseFloat(lineArray[12]) + Float.parseFloat(lineArray[13]));
        this.upvotes = (float) (Math.round(this.upvotes * 100.0) / 100.0);
        this.avg_pt = Integer.parseInt(lineArray[14]);
        this.developers = lineArray[15];
        this.genres = this.stringToStringArray(lineArray[16]);
    }

    private String[] stringToStringArray(String str) {
        // if (str == null) return null;
        if (str.charAt(0) == '[' &&
        str.charAt(str.length() - 1) == ']')
            str = str.substring(1, str.length() - 1);
        str = str.replace("'", "").replace(", ", ",");
        return str.split(",");
    }

    private String intToTimeString(int intTime) {
        if (intTime == 0) return null;
        int hours = intTime/60;
        int minutes = intTime - hours * 60;
		String stringTime = "";
		if (hours != 0 && minutes != 0)
			stringTime = Integer.toString(hours) + "h " + Integer.toString(minutes) + 'm';
		else if (hours != 0)
        	stringTime = Integer.toString(hours) + 'h';
        else if (minutes != 0)
            stringTime += Integer.toString(minutes) + 'm';
		else
			stringTime = null;
        return stringTime;
    }

	public void imprimir() {
        String[] stringArray = this.release_date.toString().split(" ");
        String dateString = stringArray[1] + '/' + stringArray[stringArray.length - 1];
		String price = Float.toString(this.price);
		if (price.charAt(price.length() - 2) == '.' && price.charAt(price.length() - 1) == '0')
			price += '0';
        String website = this.website.length() == 0 ? null : this.website;
        String upvotes = Integer.toString((int) (this.upvotes * 100)) + '%';
        String avg_pt = this.intToTimeString(this.avg_pt);

		MyIO.println(
            this.app_id + " " + this.name + " " + dateString + " " +
			this.owners + " " + this.age + " " + price + " " +
			this.dlcs + " " + Arrays.toString(this.languages) + " " + website + " " +
			this.windows + " " + this.mac + " " + this.linux + " " +
			upvotes + " " + avg_pt + " " + this.developers + " " +
			Arrays.toString(this.genres)
		);
	}

    public Game clone() {
        return new Game(
            this.app_id, this.name, this.release_date, this.owners,
            this.age, this.price, this.dlcs, this.languages.clone(),
            this.website, this.windows, this.mac, this.linux,
            this.upvotes, this.avg_pt, this.developers, this.genres.clone()
        );
    }

	public int getApp_id() {
		return this.app_id;
	}

	public String getName() {
		return this.name;
	}

	public Date getRelease_date() {
		return this.release_date;
	}

	public String getOwners() {
		return this.owners;
	}

	public int getAge() {
		return this.age;
	}

	public float getPrice() {
		return this.price;
	}

	public int getDlcs() {
		return this.dlcs;
	}

	public String[] getLanguages() {
		return this.languages;
	}

	public String getWebsite() {
		return this.website;
	}

	public boolean getWindows() {
		return this.windows;
	}

	public boolean getMac() {
		return this.mac;
	}

	public boolean getLinux() {
		return this.linux;
	}

	public float getUpvotes() {
		return this.upvotes;
	}

	public int getAvg_pt() {
		return this.avg_pt;
	}

	public String getDevelopers() {
		return this.developers;
	}

	public String[] getGenres() {
		return this.genres;
	}

	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setDlcs(int dlcs) {
		this.dlcs = dlcs;
	}

	public void setLanguages(String[] languages) {
		this.languages = languages;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setWindows(boolean windows) {
		this.windows = windows;
	}

	public void setMac(boolean mac) {
		this.mac = mac;
	}

	public void setLinux(boolean linux) {
		this.linux = linux;
	}

	public void setUpvotes(float upvotes) {
		this.upvotes = upvotes;
	}

	public void setAvg_pt(int avg_pt) {
		this.avg_pt = avg_pt;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}	
}
