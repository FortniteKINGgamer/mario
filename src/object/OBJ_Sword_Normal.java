package object;

import adam.entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

public OBJ_Sword_Normal(GamePanel gp){
    super(gp);

     name = "Normal Sword";
     down1 = setUp("/objects/sword_normal",gp.tileSize,gp.tileSize);
     attackValue = 1;



}
}