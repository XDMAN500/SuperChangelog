package me.varmetek.plugin.superchangelog.gui;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.varmetek.plugin.superchangelog.Changelog;
import me.varmetek.plugin.superchangelog.ChangelogComponent;
import me.varmetek.plugin.superchangelog.ChangelogManager;
import me.varmetek.plugin.superchangelog.utility.ReflectionTool;
import me.varmetek.plugin.superchangelog.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class ChangelogGui implements Listener
{

  protected static ReflHelp reflHelp = null;
  protected ChangelogManager changeLogManager;

  public static final int
    MAX_CHARACTERS = 20,
    MAX_LINES = 14;



  public ChangelogGui (ChangelogManager changeLogManager){
    this.changeLogManager = changeLogManager;
    if(reflHelp == null){
      reflHelp = new ReflHelp();
    }
  }

  protected ItemStack getChangeLogIcon(Changelog log){
    ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
    bookMeta.setAuthor("Server");
    bookMeta.setGeneration(BookMeta.Generation.ORIGINAL);
    bookMeta.setTitle(log.getTitle().getMessage());


    int lines = 0;

    //Adding pages

    StringBuilder pageData = new StringBuilder();

    for (ChangelogComponent comp : log.getComponents()) {




      int allocLine = (comp.getMessageNoColor().length() / MAX_CHARACTERS) + 1; //How many lines the string uses
      if (lines + allocLine <= MAX_LINES){

        lines += allocLine;
        pageData.append(comp.getMessage()).append('\n');
      } else {
        bookMeta.addPage(pageData.toString());


        pageData = new StringBuilder();

        lines = allocLine;
        pageData.append(comp.getMessage()).append('\n');

      }


    }
    bookMeta.addPage(pageData.toString());
    bookMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    pageData = null;


    itemStack.setItemMeta(bookMeta);
    return itemStack;

  }

  public void openBook(ItemStack book, Player p){
   int slot = p.getInventory().getHeldItemSlot();
    ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
    ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);

        reflHelp.sendPacket(p,buf);

        p.getInventory().setItem(slot, old);
  }


  protected Inventory genBlankInventory(ChangelogManager clM){
      int rows = Math.min(clM.getChangelogs().size()/9 +1, 6);
      return Bukkit.createInventory(null,9*rows);
  }

  public BookGui getNewGui(){
    return new BookGui();
  }

  @EventHandler
  public void inventoryHandler(InventoryClickEvent ev){
    Inventory inv = ev.getView().getTopInventory();
    if(inv == null ) return;
    if(!(inv.getHolder() instanceof BookGui)) return;
    Player player = (Player)ev.getWhoClicked();
   ev.setCancelled(true);
   ItemStack book = ev.getCurrentItem();
     if(book == null) return;
    if(book.getType() != Material.WRITTEN_BOOK) return;
    openBook(book,player);

  }



  public class BookGui implements InventoryHolder
  {
    protected Inventory inv;


    protected BookGui(){

      int rows = Math.min(changeLogManager.getChangelogs().size()/9 +1, 6);
     inv = Bukkit.createInventory(this,9*rows, TextUtil.color("&6Change Logs"));
      List<Changelog> logs = changeLogManager.getChangelogs().reverse().subList(0, Math.min(changeLogManager.getChangelogs().size(),inv.getSize()));
      for(Changelog log : logs){
        inv.addItem(getChangeLogIcon(log));
      }

    }
    @Override
    public Inventory getInventory (){
      return inv;
    }
  }




  protected class ReflHelp{


    //Try to imitate this code
    //
    //  PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
    //    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);

    ReflectionTool.ClassAccessor dataSerClass;
    ReflectionTool.ConstructorAccessor dataSerConstructor;
    Object dataSerObject;

    ReflectionTool.ClassAccessor payLoadPacketClass;
    ReflectionTool.ConstructorAccessor payLoadPacketConstructor;
    Object payLoadPacketObject;


    ReflectionTool.ClassAccessor craftPlayerClass;
    ReflectionTool.MethodAccessor getHandeMethod;

    ReflectionTool.ClassAccessor entityPlayerClass;
    ReflectionTool.FieldAccessor playerConField;

    ReflectionTool.ClassAccessor playerConnectClass;
    ReflectionTool.MethodAccessor sendPacketMethod;

    ReflectionTool.ClassAccessor genericPacketClass;


    protected ReflHelp(){
      dataSerClass =ReflectionTool.getNmsClass("PacketDataSerializer");
      dataSerConstructor = dataSerClass.findConstructor(ByteBuf.class);

      payLoadPacketClass = ReflectionTool.getNmsClass("PacketPlayOutCustomPayload");
      payLoadPacketConstructor = payLoadPacketClass.findConstructor(String.class, dataSerClass.getContainedClass());

      craftPlayerClass = ReflectionTool.getObcClass("entity.CraftPlayer");
      getHandeMethod = craftPlayerClass.findMethod("getHandle");

      entityPlayerClass = ReflectionTool.getNmsClass("EntityPlayer");
      playerConField = entityPlayerClass.findField("playerConnection");

      playerConnectClass = ReflectionTool.getNmsClass("PlayerConnection");
      genericPacketClass = ReflectionTool.getNmsClass("Packet");
      sendPacketMethod = playerConnectClass.findMethod("sendPacket",genericPacketClass.getContainedClass());

    }

    public void sendPacket(Player p, ByteBuf data){
      Preconditions.checkNotNull(p);
      Object dataSerObject = dataSerConstructor.invoke(data);
      Object payLoadPacketObject = payLoadPacketConstructor.invoke("MC|BOpen",dataSerObject);
      Object craftPlayerObject =  p;
      Object entityPlayerObject = getHandeMethod.invoke(craftPlayerObject);
      Object playerConObject = playerConField.getValue(entityPlayerObject);
      sendPacketMethod.invoke(playerConObject,payLoadPacketObject);


    }
  }
}
