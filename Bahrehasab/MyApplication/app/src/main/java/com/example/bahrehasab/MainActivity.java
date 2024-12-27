package com.example.bahrehasab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText text;
    private TextView outp;
    private EditText mymonth;
    private EditText myday;
    String res="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text=(EditText)findViewById(R.id.input);
        outp=(TextView)findViewById(R.id.output);
        mymonth=(EditText)findViewById(R.id.mymon);
        myday=(EditText)findViewById(R.id.mydate);

        ImageView logoImage = findViewById(R.id.logoImage);
        logoImage.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        logoImage.setClipToOutline(true);

        Button myButton1 = findViewById(R.id.button1);
        myButton1.setBackgroundResource(R.drawable.button_background);
        Button myButton2 = findViewById(R.id.button2);
        myButton2.setBackgroundResource(R.drawable.button_background);
        Button myButton3 = findViewById(R.id.button3);
        myButton3.setBackgroundResource(R.drawable.button_background);
        Button myButton4 = findViewById(R.id.button4);
        myButton4.setBackgroundResource(R.drawable.button_background);//
        Button myButton5 = findViewById(R.id.button5);
        myButton5.setBackgroundResource(R.drawable.button_background);// Set background drawable programmatically


    }


    String s1 = "";
    String s2 = "";
    String s3 = "";
    String s4 = "";
    String s5 = "";
    String s6 = "";
    String s7 = "";
    String s8 = "";
    String s9 = "";
    String s10 = "";
    String s11 = "";
    String s12 = "";
    String s13 = "";
    String s14 = "";
    String s15 = "";
    String s16 = "";
    String s17 = "";
    String s18 = "";
    String s19 = "";

    public String[] days ={"ሰንበት","ሰኑይ",
            "ሰሉስ","ረቡዕ","ሓሙስ","ዓርቢ","ቀዳም"};
    public final int BuluyKidan = 5500;
    public int Rabiet;
    public int monthn;
    public int metqi;
    public String[] months ={"መስከረም","ጥቅምቲ","ሕዳር","ታሕሳስ","ጥሪ",
            "ለካቲት","መጋብት","ማዝያ","ጉንበት","ሰነ","ሓምለ","ነሓሰ"};
    int year = 0;
    String s = "  ,";
    public void hafie(View v){
        res=
                "Developed by:Haftom Abrha."+
                        "\nfirst degree: softwore"+
                        "Engineering.";
        outp.setText(res);

    }
    public void info(View v){
        outp.setText(
                "ኣፅዋማትን በዓላትን ንምድላይ ዓመተ"+
                        "\nምህረት ብምእታው ኣፅዋማትን በዓላትን"+
                        "\nዝብል በተን ፅቀጥ።\nማዕልቲ ልደት"+
                        " ንምድላይ "+
                        " ዓመተ ምህረት፣ወርሒን ማዕልቲን ብምእታው"
                        +" ማዕልቲ ዝብል በተን ፅቀጥ።"+
                        "\nከም ሓድስ ንምጅማር ኣጥፍእ ዝብል በተን" +" ፅቀጥ።\n developer ንምፍላጥ"+
                        " about ዝብል በተን ፅቀጥ።");
    }

    public void bahre(View v){
        try
        {
            String in=text.getText().toString();

            if (in!= "")
            {
                int d =Integer.parseInt(in);
                Rabiet = 0;
                monthn = 0;
                metqi = 0;
                String s = ",";

                if (d >= 33)
                {
                    int AmeteAlem = d + BuluyKidan;
                    Rabiet = AmeteAlem /4; int Kemer = Rabiet + AmeteAlem;
                    int medeb, wenber;
                    if (AmeteAlem % 19 == 0)
                    {
                        medeb =0;
                        wenber =18;
                    }
                    else if (AmeteAlem % 19 == 1)
                    {
                        medeb =1;
                        wenber = 0;
                    }
                    else
                    {
                        medeb =AmeteAlem % 19;
                        wenber =medeb -1;
                    }

                    int y =wenber * 19;
                    if (y < 30)
                        metqi =y;
                    else
                        metqi = y % 30;
                    int reused = 0;
                    int mebajaHamer = 0;
                    int[] date = new int[20];
                    int[] month = new int[20];
                    if(verfiyMetqi())
                    {
                        if (metqi < 14)
                        {
                            month[0] = 1;
                            date[0] =show2(d, month[0], metqi);
                        }
                        else
                        {
                            month[0] = 0;
                            date[0] = show2(d, month[0], metqi);
                        }

                        if (date[0] == 6)
                            mebajaHamer = (metqi + 8);
                        else
                            mebajaHamer = (metqi + (7 - date[0]));
                        reused = mebajaHamer;
                        /*                           // finding mebahajahamer used for all }finding festivals and fasting. */
                        if ((((mebajaHamer >= 17) && (month[0] == 0)) && (mebajaHamer <= 30)))
                        { /* //mebajahamer is not allowd befor 17 tiri.*/
                            month[1] = 4;
                            mebajaHamer = mebajaHamer;
                            date[1] =show2(d, month[1], mebajaHamer);
                        }
                        else if (mebajaHamer > 30 || (mebajaHamer <= 21 && month[0] == 1))
                        {
                            mebajaHamer =mebajaHamer % 30;
                            month[1] =5;
                            date[1] = show2(d, month[1], mebajaHamer);
                        }
                    }

                    s1 = "ፆመ ነነዌ   ↔" + days[date[1]] + s + mebajaHamer + s + months[month[1]] + s + d + "ዓ/ም";

                    int aby = mebajaHamer + 14;
                    if (aby > 30 && month[1] == 4)
                    {
                        month[2] = 5;
                        aby = aby % 30;
                        date[2] = show2(d, month[2], aby);
                    }
                    else if (aby > 30 && month[1] == 5)
                    {
                        aby = aby % 30;
                        month[2] = 6;
                        date[2] = show2(d, month[2], aby);
                    }
                    else if (aby < 30 && month[1] == 5)
                    {
                        month[2] = 5;
                        aby = aby % 30;
                        date[2] = show2(d, month[2], aby);
                    }

                    s2 = "ዓብዪ ፆም  ↔" + days[date[2]] + s + aby + s + months[month[2]] + s + d + "ዓ/ም";
                    int Haweria = reused + 29;
                    if ((Haweria % 30) <= 20 && month[1] == 5)
                    {
                        Haweria %= 30;
                        month[9] = 9;
                        date[9] = show2(d, month[9], Haweria);
                    }
                    else if ((((Haweria % 30) >= 16) && ((Haweria % 30) <= 28)))
                    {
                        Haweria %= 30;
                        month[9] = 8;
                        date[9] =show2(d, month[9], Haweria);
                    }
                    s3 = "ፆመሓውርያት ↔" + days[date[9]] + s + Haweria + s + months[month[9]] + s + d + "ዓ/ም";
                    int nebeya;
                    int callander = BuluyKidan + year;
                    if (callander % 4 == 0)
                    {
                        nebeya = 14;
                        month[11] = 2;
                        date[11] =show2(d, month[11], nebeya);
                    }
                    else
                    {
                        nebeya = 15;
                        month[11] = 2;
                        date[11] =show2(d, month[11], nebeya);
                    }
                    s4 = "ፆመ ነበያት   ↔" + days[date[11]] + s + nebeya + s + months[month[11]] + s + d + "ዓ/ም";
                    int fls = 1;                       month[12] = 11;
                    date[12] = show2(d, month[12], fls);
                    s5 = "ፆመ ፍልሰታ  ↔" + days[date[12]] + s + fls + s + months[month[12]] + s + d + "ዓ/ም";
                    int meskel = 17;
                    month[13] = 0;
                    date[13] = show2(d, month[13], meskel);
                    s15 = " መስቀል   ↔" + days[date[13]] +s+ meskel + s + months[month[13]] + s + "ዓ/ም";
                    int lidet;
                    if (AmeteAlem % 4 == 0)
                    {
                        lidet = 28;
                        month[11] = 3;
                        date[11] =show2(d, month[11], lidet);
                    }
                    else
                    {
                        lidet = 29;
                        month[11] = 3;
                        date[11] =show2(d, month[11], lidet);
                    }

                    s6 = " ልደት/ገና   ↔" + days[date[11]] + s + lidet + s + months[month[11]] + s + d + "ዓ/ም";

                    int timket = 11;
                    month[14] = 4;
                    date[14] = show2(d, month[14], timket);
                    s16 = " ጥምቀት   ↔" + days[date[14]] + s + timket + s + months[month[14]] + s + d + "ዓ/ም";

                    int meriam = 1;
                    month[15] = 8;
                    date[15] =show2(d, month[15], meriam);
                    s17 = "ማርያም ጉንቤት ↔" + days[date[15]] + s + meriam + s + months[month[15]] + s + d + "ዓ/ም";

                    int ashenda = 17;
                    month[16] = 11;
                    date[16] =show2(d, month[16],ashenda);
                    s18 = "  ኣሸንዳ    ↔" + days[date[16]] + s + ashenda + s + months[month[16]] + s + d + "ዓ/ም";

                    int Tinsae = mebajaHamer + 9;
                    if ((Tinsae >= 26 && Tinsae <= 30) && month[1] == 4)
                    {
                        month[6] = 6;
                        Tinsae =Tinsae;
                        date[6] = show2(d, month[6], Tinsae);
                    }
                    else if (Tinsae > 30 && month[1] == 5)
                    {
                        month[6] = 7;
                        Tinsae =Tinsae% 30;
                        date[6] = show2(d, month[6], Tinsae);
                    }
                    else if (Tinsae < 30 && month[1] == 5)
                    {
                        month[6] = 7;
                        Tinsae = Tinsae%30;
                        date[6] = show2(d, month[6], Tinsae);
                    }
                    else
                    {
                        month[6] = 7;
                        Tinsae = Tinsae%30;
                        date[6] =show2(d, month[6], Tinsae);

                    }
                    s7 = "  ፋስካ    ↔" + days[date[6]] + s + Tinsae + s + months[month[6]] + s + d + "ዓ/ም";
                    int hawer = 5;
                    month[10] = 10;
                    date[10] = show2(d, month[10], hawer);
                    s8 = "   ሓወርያ  ↔" + days[date[10]] + s + hawer + s + months[month[10]] + s + d + "ዓ/ም";

                    int Hosaena = mebajaHamer + 2;
                    if ((Hosaena >= 19 && Hosaena <= 29) && month[1] == 4)
                    {
                        month[4] = 6;
                        Hosaena = Hosaena;
                        date[4] = show2(d, month[4], Hosaena);
                    }
                    else if (Hosaena >= 30 || (Hosaena <= 23 && month[1] == 5))
                    {
                        Hosaena %=30;
                        month[4] = 7;
                        date[4] = show2(d, month[4], Hosaena);
                    }
                    s9 = "    ሆሳእና   ↔" + days[date[4]] + s + Hosaena + s + months[month[4]] + s + d + "ዓ/ም";
                    int debrezeyt = reused + 11;
                    if (debrezeyt >= 28 && debrezeyt <= 30)
                    {/* / debrezeyt befor lekatat 28 not allowed.
                     */

                        debrezeyt = debrezeyt;
                        month[3] = 6;

                        //one mines frm array
                        date[3] = show2(d, month[3], debrezeyt);
                        //finding the day.
                    }
                    else if (debrezeyt > 30 || debrezeyt <= 24)
                    {
                        month[3] = 6;
                        debrezeyt = debrezeyt % 30;
                        date[3] = show2(d, month[3], debrezeyt);
                    }
                    else
                    {
                        month[3] = 6;
                        debrezeyt =debrezeyt;
                        date[3] = show2(d, month[3], debrezeyt);
                    }

                    s10 = " ደብረዘይቲ ↔" + days[date[3]] + s + debrezeyt + s + months[month[3]] + s + d + "ዓ/ም";
                    int goodFriday=reused+7;
                    if ((goodFriday >= 24 && goodFriday < 30) && month[1] == 4)
                    {
                        goodFriday=goodFriday;
                        month[5] = 6;
                        date[5] = show2(d, month[5], goodFriday);
                    }
                    else if (goodFriday >= 30 || (goodFriday <= 28 && month[1] == 5))
                    {
                        goodFriday %= 30;
                        month[5] = 7;
                        date[5] = show2(d, month[5], goodFriday);
                    }// GOOD friday
                    s11 = "ዓርቢ ስቅለት ↔" + days[date[5]] + s + goodFriday + s + months[month[5]] + s + d + "ዓ/ም";
                    int ERIGET = mebajaHamer+ 18;
                    if ((ERIGET % 30) <= 9 && month[1] == 5)
                    {
                        month[8] = 9;
                        ERIGET %= 30;
                        date[8] = show2(d, month[8], ERIGET);
                    }
                    else if ((ERIGET % 30) <= 17 && (ERIGET % 30 >= 5))
                    {
                        month[8] = 8;
                        ERIGET %= 30;
                        date[8] = show2(d, month[8], ERIGET);
                    }
                    else if (ERIGET % 30 >= 20 && ERIGET % 30 <= 29)
                    {
                        month[8] = 8;
                        ERIGET %= 30;
                        date[8] = show2(d, month[8], ERIGET);
                    }
                    s12 = "   እርገት    ↔" + days[date[8]] + s + ERIGET + s + months[month[8]] + s + d+"ዓ/ም";
                    int Rekibe_Kahinat = mebajaHamer + 3;
                    if ((Rekibe_Kahinat >= 20 && Rekibe_Kahinat < 30) && month[1] == 4)
                    {
                        month[7] = 7;
                        Rekibe_Kahinat = Rekibe_Kahinat;
                        date[7] = show2(d, month[7], Rekibe_Kahinat);
                    }

                    else if (Rekibe_Kahinat > 30 || Rekibe_Kahinat <= 24)
                    {
                        month[7] = 8;
                        Rekibe_Kahinat %= 30;
                        date[7] = show2(d, month[7], Rekibe_Kahinat);
                    }
                    s13 = "ርከበ-ካህናት↔" + days[date[7]] + s + Rekibe_Kahinat + s + months[month[7]] + s +d +"ዓ/ም";
                    String w = Wengel(d);
                    s14 = "  ወንጌል ↔" + w+ "።";
                    int hans = 1;
                    month[19] =0;
                    date[19] = show2(d, month[19], hans);
                    s19 = "ሓድስዓመት↔" + days[date[19]] + s + hans + s + months[month[19]] + s +d+"ዓ/ም";

                    in="ኦርቶዶክሳዊ ኣፅዋማት፡-\n"+                        "===================="
                            +"\n" + s3 + "\n" + s1 + "\n" + s2 +
                            "\n" + s4 + "\n" + s5 + "\n" +
                            "\n" +"ኦርቶዶክሳዊ በዓላት፦" +
                            "\n====================="+
                            "\n" + s19 + "\n" + s15+ "\n" + s6 + "\n" +
                            s16 + "\n" + s10+ "\n" + s9 + "\n" +
                            s11 + "\n" + s7 +"\n" + s13 + "\n" +s17+"\n"+
                            s12 + "\n" + s8 +"\n" + s18 + "\n" + s14;

                    outp.setText(in);

                }
                else{
                    res="ስሕተት!ዓመት ምህረት"+
                            " ትሐቲ 33 አይፍቅድን።"+
                            "\nምኽንያቱ ቅድሚ ሰቀለት ክርስቶስ"+ "ሕረሓሳብ አይነበረን።";
                    outp.setText(res);

                }
            }

            else{
                res="ስሕተት!ምንም ዝበሃል"+
                        "ቁፅሪ ኣየእተውካን።";
                outp.setText(res);
            }
        }
        catch (Exception ex)
        {

            res="ስሕተት ተረኽቡ፡ ቁፅሪ"+
                    " ጥራሕ ኣእትው።";
            outp.setText(res);

        }

    }
    public void reset(View v){

        text.setText("");

        mymonth.setText("");
        myday.setText("");
        outp.setText("");

    }
    public void findDay(View v){
        try
        {
            int ye =Integer.parseInt(text.getText().toString());
            int months =Integer.parseInt(mymonth.getText().toString());
            int dates = Integer.parseInt(myday.getText().toString());
            int dayss ;
            String  da;
            if(ye>=1&& months>=1&&dates>=1)
            {
                if(months<=12&&dates<=30) {
                    dayss=show3(ye, months, dates);
                    da=days[dayss];
                    res= "እቲ ማዕልቲ " +da+" እዩ።";
                    outp.setText(res);
                }
                else {
                    res="ወርሒ ልዐሊ 12 ወይ ከዓ"+
                            " ዕለት ልዕሊ 30 እንተኾይኑ "+
                            "ኣይፍቀድን።";
                    outp.setText(res);
                }
            }


            else{
                res="ዓመተ ምህረት፣ወርሕን ዕለትን "+
                        "ትሕቲ 1 ኣይፍቀድን።";
                outp.setText(res);
            }
        }

        catch (Exception ex)
        {
            res ="ዓመት ምህረት ፣ወርሕን ዕለትን"+
                    " ኣእትው።";
            outp.setText(res);
        }

    }

    public boolean verfiyMetqi()
    {
        int[] invalid = { 1, 3, 6, 9, 11, 17, 20, 22, 25, 28 };
        for (int i = 0; i < 10; i++)
        {
            if (metqi == invalid[i])
                return false;
        }

        return true;
    }
    public int show2(int year, int month, int date)
    {
        int atsife = 2 * (month);
        int AmeteAlem =BuluyKidan + year;
        int r = AmeteAlem / 4;
        int d = r + AmeteAlem;
        int timeon = (d % 7) - 1;
        return (date + atsife + 1 + timeon) % 7;
    }
    public int COMPUTING(int year)
    {
        int AmeteAlem = BuluyKidan + year;
        int Rabie = AmeteAlem / 4;
        int kemer = Rabie + AmeteAlem;//5500+2012/4)+
        return kemer % 7 + 1;
    }
    public String Wengel(int year)
    {
        int callander = BuluyKidan + year;
        if (callander % 4 == 0)
            return "ዮሃንስ";

        else if (callander % 4 == 1)
            return "ማቴዎስ";
        else if (callander % 4 == 2)
            return "ማርቆስ";
        else
            return "ልቋስ";
    }
    public int show3(int year, int month, int dat)
    {
        int date = (COMPUTING(year) + (month - 1) * 30 + (dat - 1)) % 7;
        return date;
    }


}


