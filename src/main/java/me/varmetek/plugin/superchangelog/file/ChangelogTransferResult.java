package me.varmetek.plugin.superchangelog.file;

import com.google.common.base.Preconditions;
import me.varmetek.plugin.superchangelog.utility.Constants;

public final class ChangelogTransferResult
{


  //holds the type of transfer
  protected TransferType type;

  protected ErrorContext context;



  protected ChangelogTransferResult (TransferType type, ErrorContext errorContext ){
   this.context = errorContext;
   this.type = type;
  }

  public boolean hasErrorContext(){
    return context != null;
  }

  public ErrorContext getContext(){
    return context;
  }

  public boolean isSuccessful(){
    return context == null;
  }

  public TransferType getTransferType(){
    return type;
  }


  public static ChangelogTransferResult getSuccess(TransferType type){
    return new ChangelogTransferResult(Preconditions.checkNotNull(type),null);
  }
  public static ChangelogTransferResult getFailure(TransferType type, ErrorContext context){
    return new ChangelogTransferResult(Preconditions.checkNotNull(type),Preconditions.checkNotNull(context));
  }



  public enum TransferType{
    YAML,HJSON,NEITHER
  }

  public static class ErrorContext{
    protected String message;
    protected Exception exception;
    protected TransferType type;
    //is true if requested transfer from file doesn't exist
    protected boolean nothingToTransfer;

    public ErrorContext (boolean nothingToTransfer, TransferType typel){
      this(nothingToTransfer,typel,null);

    }

    public ErrorContext (boolean nothingToTransfer, TransferType typel, Exception ex){
      this(nothingToTransfer,typel,ex,createMessage(nothingToTransfer,typel,ex));
    }

    public ErrorContext (boolean nothingToTransfer, TransferType typel, Exception ex, String error){
      this.nothingToTransfer = nothingToTransfer;
      this.message = error;
      this.exception = ex;
      this.type = type;

    }


    public boolean wasTransferFilePresent(){
      return nothingToTransfer;
    }

    public String getMessage(){
      return message;
    }

    public Exception getException(){
      return exception;
    }

  }

  protected static String createMessage(boolean nothingToTransfer, TransferType type, Exception ex){
    StringBuilder finalMessage = new StringBuilder("Failed to transfer changelogs: ");

    if(type != TransferType.NEITHER && nothingToTransfer){

        finalMessage.append("Could not find ");
        switch (type){
          case YAML: {finalMessage.append(Constants.CHANGELOGFILE_YML);}break;
          case HJSON: {finalMessage.append(Constants.CHANGELOGFILE_HJSON);}break;
            default:{finalMessage.append("changelog file");}
        }
      }else{
      if(ex == null){
        finalMessage.append("Unknown error");
      }else {
        finalMessage.append('[').append(ex.getClass().getName()).append(']').append(' ').append(ex.getMessage());
      }

      }
      return finalMessage.toString();
  }


}
