import org.bouncycastle.jcajce.provider.digest.SM3;
import org.junit.jupiter.api.Test;
import Hash.Sm3Utils;


public class MainTest {

    @Test
    public void sm3Test(){
        String str = "Hello World!";
        String encStr = Sm3Utils.encrypt(str);
        System.out.println(Sm3Utils.verify(str, encStr));
    }
}
