
import java.util.Scanner;


class Main
{

      

  public static void main(String[] args) 
  {      
	System.out.println("Bem vindo ao AshleyEscapeRoom!");
	System.out.println("Chegue ate o final, evitando paredes e armadilhas invisiveis,");
	System.out.println("Recolha todas as ervas!");

        
  

    GameGUI game = new GameGUI();
    game.createBoard();
    
 



    int m = 60; 

    int px = 0;
    int py = 0; 
    
    int ponto = 0;

    Scanner in = new Scanner(System.in);
    String[] validCommands = { "direita", "esquerda", "cima", "baixo", "d", "e", "c", "b",
    "pular", "pd", "pularesquerda", "pe", "pularcima", "pc", "pularbaixo", "pb",
    "pegar", "p", "sair", "s", "replay", "ajuda", "?"};
  

    boolean play = true;
    while (play){

    System.out.println("Entre um comando: ");

    String input = UserInput.getValidInput(validCommands);
    if (input.equals("sair")|| input.equals("s")){
    play = false;
    }
    else if (input.equals("replay"))
    {
    ponto += game.replay();
    System.out.println("ponto=" + ponto);
    System.out.println("passos=" + game.getSteps());
    
    System.out.println("ponto foi resetado");
    ponto = 0;

    }
    else if (input.equals("ajuda")|| input.equals("h")|| input.equals("?"))
    {
      System.out.println("Comandos validos: " + "direita, esquerda, cima, baixo, d, e, c, b" + "pular pd, pularesquerda, pe, pularcima, pc, pularbaixo, pb " + "pegar, p, sair, s, replay, ajuda, ?");
    }
     else if  (input.equals("pegar")|| input.equals("p"))
     {
        ponto += game.pickupPrize();
     }

     if (input.equals("direita")|| input.equals("d")) 
     {
       
       px= m;
       py= 0;

      

     }
      
       else if (input.equals("pulardireita")|| input.equals("pd")) 
     {
       
       px= 2*m;
       py= 0;
       
       

     }
  
     else if  (input.equals("esquerda")|| input.equals("e"))
     {

      px= -m;
      py= 0;

        
    }
    
      else if  (input.equals("pularesquerda")|| input.equals("pe"))
     {

      px= 2*-m;
      py= 0;

       
    }
    
     else if  (input.equals("cima")|| input.equals("c"))
     {

      px= 0;
      py= -m;

     

    }
    
     else if  (input.equals("baixo")|| input.equals("b"))
     {

      px= 0;
      py= m;

       
    }

    else if (input.equals("pularcima")|| input.equals("pc")) {

      px = 0;
      py = -2*m;

        
    }

    else if (input.equals("pularbaixo")|| input.equals("pb")){

      px = 0;
      py = 2*m;

      
    }

if(game.isTrap(px,py))
{

  ponto += game.springTrap(px,py);

}
  
  ponto +=game.movePlayer(px,py);     
    
      }

     
        ponto += game.endGame();

      System.out.println("ponto=" + ponto);
      System.out.println("passos=" + game.getSteps());
    }
   
}
