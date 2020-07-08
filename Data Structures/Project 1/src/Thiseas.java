//Written By Dimitrios Delikouras 3170034
//Evangelos Venetasnos 3180019



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Thiseas {
	static StringStackImpl<String[]> dotStack = new StringStackImpl<String[]>();
	static int limits[] = new int[2];
	static String entrance[] = new String[4];
	static String item[] = new String[3];


	public static String[][] readMap(String fileName) throws IOException {
		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
		int lines = 0, columns = 0;
		String str;
		int counter;
		int count_E = 0;

		str = br.readLine();
		String[] temp = str.split(" ");
		limits[0] = Integer.parseInt(temp[0]);
		limits[1] = Integer.parseInt(temp[1]);


		String[] maze[] = new String[limits[0]*limits[1]][4];

		str = br.readLine();
		String[] entrance_coordinates = str.split(" ");
		entrance[0] = entrance_coordinates[0];
		entrance[1] = entrance_coordinates[1];
		entrance[2] = "E";
		entrance[3] = "0";

		int position=0;
		while ((str = br.readLine()) != null && str.length() != 0) {
		  lines++;

			String[] line = str.split(" ");
			counter=0;

			for(String item : line){

				if(!item.contentEquals("0")&&!item.contentEquals("1")&&!item.contentEquals("E")){
					error(1);
				}

				if(item.contentEquals("E")){
					count_E++;

					if(lines-1!=Integer.parseInt(entrance[0]) || (counter)!=Integer.parseInt(entrance[1])){
						error(2);
					}
				}
				String[] cell = {String.valueOf(lines-1), String.valueOf(counter), item, "0"};
				counter+=1;

 				maze[position]= cell;
 				position+=1;
			}
			columns = counter;
			if(columns!=limits[1]){
				error(3);
			}
		   }

		   br.close();
		   if(lines!=limits[0] || columns!=limits[1]){
			  error(4);
		   }
			if(count_E != 1){
				error(5);
			}
			return maze;
	}

	public static boolean isExit(int index, String[][] maze){
		if(maze[index][2].contentEquals("E") && maze[index][3].contentEquals("0")){
			return false;
		}
		else if(maze[index][0].contentEquals("0") && maze[index][2].contentEquals("0")){
			return true;
		}
		else if(maze[index][1].contentEquals("0") && maze[index][2].contentEquals("0")){
			return true;
		}
		else if(maze[index][0].contentEquals(String.valueOf(limits[0]-1)) && maze[index][2].contentEquals("0")){
			return true;
		}
		else if(maze[index][1].contentEquals(String.valueOf(limits[1]-1)) && maze[index][2].contentEquals("0")){
			return true;
		}
		else{
			return false;
		}


	}

	public static String[] check(int index, String[][] maze){
		String option[] = new String[4];
		String coordinates[] = new String[4];

		int[] options = {index+1,index-1,index+limits[1],index-limits[1]};

		for(int value : options){
			try{
				option = maze[value];
			}
			catch(java.lang.IndexOutOfBoundsException E){
				option = maze[index];
			}



			if(option[2].contentEquals("0") && option[3].contentEquals("0")){
				coordinates = option;
				break;
			}
			else{
				coordinates = maze[index];
			}

		}

		return coordinates;
		}


	public static void solve(String[][] maze){

		String[] dot = entrance;
		int index = 0;

		for(int i=0; i<maze.length; i+=1){
			if(maze[i][2].contentEquals(entrance[2])){
				index = i;
				break;
			}
		}

		maze[index][3] = "1";
		dotStack.push(dot);



		while(!isExit(index, maze)){

			String[] tmp = check(index, maze);

			if(dot[0].contentEquals(tmp[0])&&dot[1].contentEquals(tmp[1])){


				if(dot[2].contentEquals(entrance[2])){
					error(6);
				}

				maze[index][3] = "1";
				dotStack.pop();
				dot = dotStack.peek();


			}
			else{
				maze[index][3] = "1";
				dotStack.push(tmp);
				dot = tmp;
			}
			for(int i=0; i<maze.length; i+=1){
				if(maze[i][0].contentEquals(dot[0]) && maze[i][1].contentEquals(dot[1])){
					index =i;
					break;
				}
			}


		}
		System.out.println("Exit = (" + dot[0]+", "+dot[1]+")");

	}

	public static void error(int code){
		switch(code){
			case 1:
				System.out.println("Wrong data given!");
				break;
			case 2:
				System.out.println("Wrong entrance data given!");
				break;
			case 3:
				System.out.println("Wrong matrix data given!");
				break;
			case 4:
			  System.out.println("Wrong matrix data given!");
				break;
			case 5:
				System.out.println("No entrances found!");
				break;
			case 6:
				System.out.println("No exits found!");
				break;
			default:
				System.out.println("Error!");
		}
		System.exit(2);
	}

	/*public static void main(String[] args) throws IOException{

		solve(readMap("C:\\Users\\dimit\\Project1\\src\\data.txt"));
	}

}
*/
