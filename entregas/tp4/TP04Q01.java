import java.util.Date;
import java.util.Locale;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TP04Q01 {
    public static void main(String[] args) {
		MyIO.setCharset("UTF-8");

        String[] entradas = new String[1000];
		int numEntrada = 0;
        
		do {
            entradas[numEntrada] = MyIO.readLine();
		} while (isFim(entradas[numEntrada++]) == false);
		numEntrada--;

        if (numEntrada == 0)
            return;
        
        Game game = new Game();
        GameLista gameList = new GameLista();
        ArrayList<String> fileLines = readFile("/tmp/games.csv");
        for (int i = 0; i < numEntrada; i++) {
            for (String line : fileLines) {
                if (entradas[i].equals(line.split(",")[0])) {
                    game = new Game();
                    game.ler(line);
                    gameList.inserirFim(game);
                    break;
                }
            }
        }

        GameLista gamesRemoved = new GameLista();
        numEntrada = MyIO.readInt();
        for (int i = 0; i < numEntrada; i++) {
            String[] comando_game = MyIO.readLine().split(" ");

            if (comando_game[0].charAt(0) == 'I') {
                for (String line : fileLines) {
                    if (comando_game[comando_game.length - 1].equals(line.split(",")[0])) {
                        game = new Game();
                        game.ler(line);
                        break;
                    }
                }
            }

            switch (comando_game[0]) {
                case "II":
                    gameList.inserirInicio(game);
                    break;
                case "I*":
                    gameList.inserir(game, Integer.parseInt(comando_game[1]));
                    break;
                case "IF":
                    gameList.inserirFim(game);
                    break;
                case "RI":
                    gamesRemoved.inserirFim(gameList.get(0));
                    gameList.removerInicio();
                    break;
                case "R*":
                    gamesRemoved.inserirFim(gameList.get(Integer.parseInt(comando_game[1])));
                    gameList.remover(Integer.parseInt(comando_game[1]));
                    break;
                case "RF":
                    gamesRemoved.inserirFim(gameList.get(gameList.size() - 1));
                    gameList.removerFim();
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < gamesRemoved.size(); i++) {
            MyIO.println("(R) " + gamesRemoved.get(i).getName());
        }
        
        for (int i = 0; i < gameList.size(); i++) {
            MyIO.print("[" + i + "] ");
            gameList.get(i).imprimir();
        }
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

class Celula {
    Game game;
    Celula next;

    Celula() {
        this(null);
    }

    Celula(Game game) {
        this.game = game;
        this.next = null;
    }

    void swap(Game game1, Game game2) {
        Game tmp = game1;
        game1 = game2;
        game2 = tmp;
    }
}

class GameLista {
    private Celula firstItem;
    private int size;

    GameLista() {
        this.firstItem = new Celula();
        int size = 0;
    }

    void inserir(Game game, int pos) {
        Celula item = this.firstItem;
        Celula tmp = new Celula(game);
        for (int i = 0; i < pos; item = item.next, i++);
        tmp.next = item.next;
        item.next = tmp;
        this.size++;
    }

    void inserirInicio(Game game) {
        this.inserir(game, 0);
    }

    void inserirFim(Game game) {
        this.inserir(game, this.size);
    }

    void remover(int pos) {
        if (this.size == 1) {
            this.firstItem = new Celula();
            this.size = 0;
            return;
        }
        Celula item = this.firstItem;
        for (int i = 0; i < pos; item = item.next, i++);
        item.next = item.next.next;
        this.size--;
    }

    void removerInicio() {
        this.remover(0);
    }

    void removerFim() {
        this.remover(this.size - 1);
    }

    int size() {
        return this.size;
    }
    
    Game get(int pos) {
        Celula item = new Celula();
        for (int i = 0; i < pos; item = item.next, i++);
        return item.next.game;
    }

    void set(int pos, Game game) {
        Celula item = new Celula();
        for (int i = 0; i < pos; item = item.next, i++);
        item.next.game = game;
    }

    void imprimir() {
        MyIO.print("[");
        
        MyIO.println("]");
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
