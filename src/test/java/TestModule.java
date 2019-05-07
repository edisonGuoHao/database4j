import org.junit.Test;

/**
 * @ClassName: test
 * @description:
 * @author: edison_Kwok
 * @Date: create in 2019/5/6 17:51
 * @Version: 1.0
 */

public class TestModule {

    @Test
    public void test01(){
        String s = this.trimTheLike("%5%");
        System.out.println(s);
    }


    private String trimTheLike(String searchValueLike){
        Integer likeFlag = this.tellTheLike(searchValueLike);
        if(likeFlag==3){
            return searchValueLike.substring(1,searchValueLike.length()-1);
        }else if(likeFlag==2){
            return searchValueLike.substring(0,searchValueLike.length()-1);
        }else if(likeFlag==1){
            return searchValueLike.substring(1);
        }else {
            return searchValueLike;
        }
    }

    /**
     * @Author edison_Kwok
     * @Description //TODO 分辨是什么类型模糊查询
     * @Date 2019/5/6
     * @Param [searchValueLike]
     * @Return java.lang.Integer
     **/
    private Integer tellTheLike(String searchValueLike){
        boolean startsWith = searchValueLike.startsWith("%");
        boolean endsWith = searchValueLike.endsWith("%");
        if(!endsWith&&startsWith){
            return 1;
        }else if (!startsWith&&endsWith){
            return 2;
        }else if(startsWith&&endsWith){
            return 3;
        }
        return 4;
    }
}
