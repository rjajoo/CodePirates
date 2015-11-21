package aki.codedepirates;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

/**
 * Created by User on 25-Dec-14.
 */
public class Singleton {

    private static final Singleton instance = new Singleton();

    int id;
    String password;

    SharedPreferences sharedPreferences;

    Question[] Q = new Question[14];
    Question[] M = new Question[16];
    boolean[] done = new boolean[14];
    static String[] answer = new String[16];

    int current = -1;
    int count = 0;
    static String DATABASE = "Code_Pirates";
    SQLiteDatabase database;
    static String TABLE = "Track";
    static String TABLE_Master = "Master";
    static int sn = 0;
    static int sn_master = 0;
    static int popup = 0;
    static int timer = 0;
    static boolean status = false;
    static int current_station = -1;
    static boolean spot = false;

    public static Singleton getInstance() {
        return instance;
    }

    private Singleton(){
        id = -1;
        password = "None";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void values() {
        current = sharedPreferences.getInt("current", 0);
        sn = sharedPreferences.getInt("sn", 0);
        sn_master = sharedPreferences.getInt("sn_master", 0);
        count = sharedPreferences.getInt("count", 0);
        popup = sharedPreferences.getInt("pop", 0);
        status = sharedPreferences.getBoolean("status", status);
        timer = sharedPreferences.getInt("timer", timer);
        spot = sharedPreferences.getBoolean("spot",spot);
        for(int i=0;i<14;i++){
            done[i] = sharedPreferences.getBoolean("done" + String.valueOf(i), false);
            Log.d("Done"+String.valueOf(i)+": ",String.valueOf(done[i]));
        }
    }

    public void values_save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("current", current);
        editor.putInt("sn",sn);
        editor.putInt("sn_master",sn_master);
        editor.putInt("count",count);
        editor.putInt("pop", popup);
        editor.putBoolean("status",status);
        editor.putInt("timer", timer);
        editor.putBoolean("spot", spot);
        for(int i=0;i<14;i++){
            editor.putBoolean("done" + String.valueOf(i), done[i]);
        }
        editor.commit();
    }

    public class Question{
        String Ques;
        String code;

        public Question(){
            this.Ques = "hello";
            this.code = "pirate";
        }

        public void setQues(String ques) {
            Ques = ques;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getQues() {
            return Ques;
        }

        public String getCode() {
            return code;
        }
    };

    public void generate(){
        Random r1 = new Random(), r2 = new Random();
        int ans = 0;
        if(current == -1){
            ans = r1.nextInt(14);
        }
        else if (count < 14) {
            while(true) {
                ans = r1.nextInt(14);
                if (!done[ans])
                    break;
            }
            /*while(true) {
                int zone = r1.nextInt(4);
                if (zone == current / 4) {
                    zone = r1.nextInt(4);
                }
                int i;
                for (i = 0; i < 4; i++) {
                    ans = (4 * zone + r2.nextInt(4))%14;
                    if (!done[ans])
                        break;
                }
                if(i!=4)
                    break;
            }*/
        } else {
            ans = -1;
        }
        if(ans != -1) {
            done[ans] = true;
        }
        current = ans;
        count++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("there",true);
        editor.putInt("current", current);
        editor.putInt("count", count);
        editor.putInt("sn", sn);
        editor.putInt("sn_master",sn_master);
        editor.putInt("timer", timer);
        if (ans != -1) {
            editor.putBoolean("done" + String.valueOf(ans), done[ans]);
        }
        editor.commit();
    }

    public void Insert(int status, int station, String time){
        database.execSQL("INSERT INTO " + TABLE + " VALUES(" + String.valueOf(sn) + ", " + String.valueOf(status) + ", " + String.valueOf(station) + ", '" + time + "');");
    }

    public void Insert_Master(int station){
        sn_master++;
        database.execSQL("INSERT INTO " + TABLE_Master + "(Sn, Station) VALUES(" + String.valueOf(sn_master) + ", " + String.valueOf(station) + ");");
    }

    public void Set(SQLiteDatabase sqLiteDatabase)
    {
        database = sqLiteDatabase;
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE +" (Sn INTEGER, Status BOOLEAN, Station INTEGER, TIME DATETIME);");
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_Master +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, Sn INTEGER, Station INTEGER);");

        for (int i = 0; i < done.length; i++) {
            //done[i] = false;
            Q[i] = new Question();
            M[i] = new Question();
        }
        M[14] = new Question();
        M[15] = new Question();

        QuestionSet();
        MasterSet();
        AnswerSet();
    }

    public int station(String s){
        for (int i = 0; i < 16; i++) {
            if(s.toLowerCase().equals(answer[i].toLowerCase()))
                return (i+1);
        }
        return -1;
    }

    void AnswerSet() {
        answer[0] = "FRIENDS";
        answer[1] = "GOTHAM";
        answer[2] = "PRISONBREAK";
        answer[3] = "WHITECOLLAR";
        answer[4] = "BREAKINGBAD";
        answer[5] = "GAMEOFTHRONES";
        answer[6] = "ORIGINALS";
        answer[7] = "SUITS";
        answer[8] = "SPARTACUS";
        answer[9] = "ARROW";
        answer[10] = "SHIELD";
        answer[11] = "VAMPIREDIARIES";
        answer[12] = "SHERLOCK";
        answer[13] = "CONSTANTINE";
        answer[14] = "POKEMON";
        answer[15] = "FLASH";
    }

    void QuestionSet(){
        Q[0].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint a,b=3,c=5;\n" +
                        "\tb&1;\n" +
                        "\ta=c/b;\n" +
                        "\tcout<<a;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[0].setCode("FRIENDS");

        Q[1].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int fun(int z)\n" +
                        "{\n" +
                        "\tint i;\n" +
                        "\tfor(i=0;i<z;i++)\n" +
                        "\t{\n" +
                        "\t\tz/=2;\n" +
                        "\t}\n" +
                        "\treturn i;\n" +
                        "}\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint a=fun(066);\t\t//\n" +
                        "\tcout<<dec<<a/2;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[1].setCode("GOTHAM");

        Q[2].setQues(
                "#include<stdio.h>\n" +
                        "\n" +
                        "int call(int);\n" +
                        "\n" +
                        "int main(){\n" +
                        "    int x,num=1;\n" +
                        "    x=call(num);\n" +
                        "    printf(\"%d\",x%16);\n" +
                        "    return 0;\n" +
                        "}\n" +
                        "\n" +
                        "int call(int num){\n" +
                        "    static int x=1,y;\n" +
                        "    if(num<4){\n" +
                        "         x=x+num*2;\n" +
                        "         y=call(num+1)+call(num+2)+call(num+3);\n" +
                        "    }\n" +
                        "    return x;\n" +
                        "}"
        );
        Q[2].setCode("PRISONBREAK");

        Q[3].setQues(
                "int main()\n" +
                        "{\n" +
                        "\tstruct bitfield\n" +
                        "\t{\n" +
                        "\t\tsigned int a:3;\n" +
                        "\t\tunsigned int b:13;\n" +
                        "\t\tunsigned int c:1;\n" +
                        "\t};\n" +
                        "\tstruct bitfield bit1={2,14,1};\n" +
                        "\tprintf(\"%d\",sizeof(bit1));\n" +
                        "\treturn(0);\n" +
                        "}"
        );
        Q[3].setCode("WHITECOLLAR");

        Q[4].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint a=2,b=3,c=1;\n" +
                        "\tif((b/=a,c++,a-=c))\n" +
                        "\t{\n" +
                        "\t\tcout<<a*c + b*c + c*a;\n" +
                        "\t}\n" +
                        "\telse\n" +
                        "\t{\n" +
                        "\t\tcout<<a*a + b*b + c*c;\n" +
                        "\t}\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[4].setCode("BREAKINGBAD");

        Q[5].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "//c++ 11\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint x[] = {4,5,6};\n" +
                        "\tint *p = nullptr;\n" +
                        "\tp = x;\n" +
                        "\tcout<<*(&(*(&(*p)+1))+1);\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[5].setCode("GAMEOFTHRONES");

        Q[6].setQues(
                "#include<iostream>\n" +
                        "#include<stdio.h>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint x = 15 - 0b1001;\n" +
                        "\tint y = 5;\n" +
                        "\tx=x^y;\n" +
                        "\ty=y^x;\n" +
                        "\tx=x^y;\n" +
                        "\tprintf(\"%x\", x*y - x*2 - y*2);\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[6].setCode("SUITS");

        Q[7].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint a=5,b=4;\n" +
                        "\tchar c='a';\n" +
                        "\tvoid *p = &a;\n" +
                        "\tint *q = &b;\n" +
                        "\tint **r = &q;\n" +
                        "\tcout<<(sizeof(a) + sizeof(!2) + sizeof(q) + sizeof(r) + sizeof(3.14) + sizeof(c) + sizeof(\"ab\"))%16;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[7].setCode("SPARTACUS");

        Q[8].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "#define add(x,y) x-y\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tint a=10,b=1;\n" +
                        "\tcout<<a+b*add(b,a)%3;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[8].setCode("ARROW");

        Q[9].setQues(
                "#include<iostream>\n" +
                        "#include<string>\n" +
                        "#include<sstream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tstringstream s;\n" +
                        "\ts<<\"\";\n" +
                        "\ts<<0x10;\n" +
                        "\tint a,b=16,c;\n" +
                        "\ts>>a;\n" +
                        "\tc=a/b+a-b;\n" +
                        "\ts.clear();\n" +
                        "\tstd::cout<<s.str()<<\"\\b\"<<c;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[9].setCode("SHIELD");

        Q[10].setQues(
                "#include <iostream> \n" +
                        "using namespace std; \n" +
                        "\n" +
                        "class K { \n" +
                        "public: \n" +
                        "    virtual void add_st(K* n) {\n" +
                        "      cout << \"13\";\n" +
                        "    } \n" +
                        "};\n" +
                        "\n" +
                        "class L: public K { \n" +
                        "public: \n" +
                        "    virtual void add_st(L* a) {\n" +
                        "      cout << \"7\";\n" +
                        "    } \n" +
                        "}; \n" +
                        "\n" +
                        "int main() { \n" +
                        "    L ob, ob2;\n" +
                        "    K  k, *pl = &ob; \n" +
                        "    pl->add_st(&ob2); \n" +
                        "    return 0; \n" +
                        "}"
        );
        Q[10].setCode("SHERLOCK");

        Q[11].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using std::cout;\n" +
                        "\n" +
                        "class NuTech\n" +
                        "{\n" +
                        "\tint x;\n" +
                        "public:\n" +
                        "\tNuTech(int y)\n" +
                        "\t{\n" +
                        "\t\tx=y;\n" +
                        "\t\tcout<<x;\n" +
                        "\t}\n" +
                        "\tvoid change(int z)\n" +
                        "\t{\n" +
                        "\t\tx==z;\n" +
                        "\t}\n" +
                        "\t~NuTech()\n" +
                        "\t{\n" +
                        "\t\tcout<<x*4;\n" +
                        "\t}\n" +
                        "};\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tNuTech Pirates(1);\n" +
                        "\tPirates.change(0);\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[11].setCode("CONSTANTINE");

        Q[12].setQues(
                "#include<iostream>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "#define OVERHEAD 5\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "\tstatic int trouble,sum;\n" +
                        "\twhile (trouble < OVERHEAD)\n" +
                        "\t{\n" +
                        "\t\ttrouble++;\n" +
                        "\t\tsum+=trouble;\n" +
                        "\t\tmain();\n" +
                        "\t\treturn 0;\n" +
                        "\t}\n" +
                        "\tcout<<sum;\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[12].setCode("POKEMON");

        Q[13].setQues(
                "#include<stdio.h>\n" +
                        "unsigned long int (* avg())[3]\n" +
                        "{\n" +
                        "\tstatic unsigned long int arr[3] = {15,16,14};\n" +
                        "\treturn &arr;\n" +
                        "}\n" +
                        "int main(void)\n" +
                        "{\n" +
                        "\tunsigned long int (*ptr)[3];\n" +
                        "\tptr = avg();\n" +
                        "\tprintf(\"%lu\" ,++(*(*ptr)));\n" +
                        "\treturn 0;\n" +
                        "}"
        );
        Q[13].setCode("FLASH");
    }

    void MasterSet(){
        M[4].setQues(
                "#include<bits/stdc++.h>\n" +
                        "\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "#define lin cout<<endl; \n" +
                        "#define fori(a,b) for(int i=(a);i<(b);i++)\n" +
                        "#define ford(a,b) for(int i=(a);i>=(b);i--)\n" +
                        "#define carry(head) ((head) > '0' && (head) <='9')\n"
        );

        M[8].setQues(
                "#define ret(a) return (a);\n" +
                        "#define coni(a) ((a) - '0')\n" +
                        "#define conc(a) ((a) + '0')\n" +
                        "#define tern(a,b,c) ((a))? (b) : (c)\n" +
                        "typedef string ss;\n" +
                        "\n" +
                        "class SmallNo\n" +
                        "{\n"
        );

        M[12].setQues(
                "\tss str;\n" +
                        "\tpublic:\n" +
                        "\tSmallNo(){};\n" +
                        "\tSmallNo(char*);\n" +
                        "\tSmallNo(int);\n" +
                        "\tvoid display();\n" +
                        "\tss getstr();\n" +
                        "\tfriend SmallNo operator*(SmallNo,SmallNo); \n" +
                        "\tfriend SmallNo operator*(SmallNo,int); \n"
        );

        M[13].setQues(
                "\tfriend SmallNo operator*(int,SmallNo); \n" +
                        "\tfriend SmallNo operator+(SmallNo,int); \n" +
                        "\tfriend SmallNo operator+(SmallNo,SmallNo);\n" +
                        "\tfriend SmallNo mul10(SmallNo);\n" +
                        "\tfriend void dis3(SmallNo);\n" +
                        "};\n" +
                        "\n" +
                        "SmallNo::SmallNo(char tem[])\n"
        );

        M[1].setQues(
                "\tford(strlen(tem)-1,0)\n" +
                        "\t\tstr = str + tem[i];\t\t\n" +
                        "}\n" +
                        "\n" +
                        "ss SmallNo::getstr()\n" +
                        "{ret(str)}\n"
        );

        M[10].setQues(
                "SmallNo::SmallNo(int no)\n" +
                        "{\n" +
                        "\tchar temp[50];\n" +
                        "\tsprintf(temp,\"%d\",no);\n" +
                        "\tSmallNo obj(temp);\n" +
                        "\tstr = obj.getstr();\n" +
                        "}\n" +
                        "\n" +
                        "void SmallNo::display()\n"
        );

        M[7].setQues(
                "{cout<<str;}\n" +
                        "\n" +
                        "SmallNo operator*(SmallNo a,SmallNo b)\n" +
                        "{\n" +
                        "\tSmallNo obj;\n" +
                        "\tint head=0;\n" +
                        "\tfor(int i=0;i<max(a.str.size(),b.str.size());i++)\n" +
                        "\t{\n" +
                        "\t\tint ans = int(tern(i<a.str.size(),coni(a.str[i]),0)) + int(tern(i<b.str.size(),coni(b.str[i]),0)) + head;\n" +
                        "\t\thead = ans/10;\n" +
                        "\t\tans = conc(ans%10);\n" +
                        "\t\tobj.str = obj.str + char(ans);\n" +
                        "\t}\n" +
                        "\thead = conc(head);\n"
        );

        M[15].setQues(
                "\ttern(carry(head),obj.str = obj.str + char(head),obj.str = obj.str);\n" +
                        "\tret(obj);\n" +
                        "}\n" +
                        "\n" +
                        "SmallNo operator+(SmallNo a,int no)\n" +
                        "{\n"
        );

        M[2].setQues(
                "\t\tSmallNo temp;\n" +
                        "\t\tint head=0;\n" +
                        "\t\tfori(0,a.str.size())\n" +
                        "\t\t{\n" +
                        "\t\t\tint y=coni(a.str[i])*(no)+head;\n" +
                        "\t\t\thead = y/10;\n" +
                        "\t\t\ttemp.str = temp.str + char(conc(y%10));\n" +
                        "\t\t}\n"
        );

        M[9].setQues(
                "\t\thead = conc(head);\n" +
                        "\t\ttern(carry(head),temp.str = temp.str + char(head),temp.str = temp.str);\n" +
                        "\t\tret(temp);\n" +
                        "}\n" +
                        "\n" +
                        "void dis3(SmallNo a)\n" +
                        "{\n" +
                        "\tford(a.str.size()-1,3)\n"
        );

        M[14].setQues(
                "\t\tcout<<a.str[i];\n" +
                        "}\n" +
                        "\n" +
                        "SmallNo mul10(SmallNo a)\n" +
                        "{ a.str = '0' + a.str;\tret(a);\t}\n"
        );

        M[3].setQues(
                "SmallNo operator+(SmallNo a,SmallNo b)\n" +
                        "{\n" +
                        "\tSmallNo ans(\"0\");\n" +
                        "\tfori(0,b.str.size())\n" +
                        "\t{\n" +
                        "\t\tSmallNo ll = a + coni(b.str[i]);\n"
        );

        M[0].setQues(
                "\t\tfor(int j=0;j<i;j++)\n" +
                        "\t\t\tll = mul10(ll);\n" +
                        "\t\tans = ans * ll;\n" +
                        "\t}\n" +
                        "\tret(ans);\n" +
                        "}\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n"
        );

        M[5].setQues(
                "\tSmallNo a(\"1000\"),b(1),c(2),d(\"SNAIHTOK\");\n" +
                        "\td.display();\n" +
                        "\tdis3(a*c+a);\n" +
                        "\tret(0);\n" +
                        "}\n"
        );
    }
}