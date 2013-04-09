FaroAuth
========

Tool for authentication to the Mendel University Faro network

Download: http://goo.gl/HVmHG

Mas problemy s prihlasovanim do siete Faro?  
Uz ta nebavi neustale prihlasovanie do Faro siete a otvorene "popup" okna v prehliadaci?  
Mam pre teba riesenie :-)

Co je FaroAuth?
===============

FaroAuth je aplikacia, ktora automatizuje prihlasenie do siete FARO.

Preco vznikol FaroAuth?
=======================

Dovodov je viac, napriklad:
- aplikacie, ktore nepocitaju s nejakou prihlasovaciou branou, zobrazuju rozne chyby, padaju alebo zobrazia namiesto cieleneho obsahu, text s prihlasenim do FARO.
- s tym suvisi aj Windows 7 notifikacia siete, ktora oznamuje, ze klient je pripojeny k internetu, ale skutocnost je taka, ze je vzdy presmerovany na prihlasenie FARO, takze nie je online.
- poznam viac ludi, ktori sa stazuju, ze im prihlasenie do FARO v internetovom prehliadaci blbne.
- nechce sa mi zaoberat prihlasovanim a odklikavanim aplikacnych chyb pri kazdom spusteni PC.
- problemy s cache, pri neobnoveni prihlasenia, sa nechce nacitat ani pozadovana stranka ani FARO stranka. Pre niektorych nie je jednoduche restartovat prehliadac, alebo otvorit iny.
- obcas sa stane, ze namiesto obnovenia prihlasenia(znovuodoslania udajov) sa obnovi stara stranka, tym padom po par minutach prihlasenie zanikne. Dane je to nezmyselnym urcenim casu, kedy sa maju akcie previest. Obe su v rovnaky cas.
- nie kazdy chce alebo moze povolit JavaScript, alebo vyskakovacie okna.
- pri padnuti prehliadaca, je len otazka casu, kedy sa internet odpoji, bezny uzivatel nema co urobit.
- ked uzivatel nechtiac zavrie vyskakovacie okno, plati predosly pripad a teda aj to, ze ked niekto nieco stahuje a odhlasi ho tak v horsom pripade par GB dat zbytocnych, pretoze sa stahovanie prerusi.

Dufam, ze raz technici spristupnia aj inu moznost, ako takyto hrozny sposob prihlasenia k internetu. Sme na kolejich(intrakoch), nie v internetovej kaviarni. Priklady si mozu brat z mnoho inych skol.

Navod:
======

Spustit program.  
Zadat udaje k prihlaseniu(potvrdit save).  
Kliknut Log in.  
Vzhladom k tomu, ze aplikacia je spustena na pozadi, mozete okno s prihlasenim zavriet a otvorit si ho ked bude treba.
V pripade, ze chces aby ta prihlasilo hned po otvoreni, zafajkni checkbox vedla Log in.  
V pripade, ze chces program spustat po spusteni operacneho systemu, pridaj ho, alebo jeho odkaz do priecinku Po spusteni.
Pri pouzivani inej siete ako Faro(napriklad doma,v skole), program nie je nutne vypnut ani odhlasit. O to bude prijemnejsie, ked sa pripojite na FARO siet a ani nezaznamenate, ze sa treba prihlasovat.

Aplikacia potrebuje JRE7, dostupne na adrese http://www.java.com/download/

v1.7:
- implementovane automaticke obnovovanie prihlasenia  

v1.6:
- implementovane korektne odhlasenie  
- znizenie casoveho rozsahu vypadku pri znovuprihlaseni do FARO

FAQ:
====

Ako funguje tato aplikacia?
> Emuluje spravanie prehliadacov k skriptom na prihlasenie do siete Faro.

Mozem byt automaticky prihlaseny po spusteni FaroAuth?
> Ano, po zaskrtnuti policka "activation on start" budes automaticky prihlaseny pri kazdom zapnuti programu.

Mozem FaroAuth spustat pri starte operacneho systemu(Windows,Linux,..)? Kde sa nachadza priecinok Po spusteni?
> Ano, pre Windows staci dat aplikaciu, alebo jej odkaz do priecinku "Po spusteni" (Ponuka start > Vsetky programy > Pri spusteni), pre linux je mozne napriklad pridat prikaz "(java -jar cesta/k/suboru/FaroAuth.jar) &" do suboru ~/.bash_rc

Ako docielim, aby som sa uz nemusel zaoberat strankami Faro v prehliadaci?
> Stiahnes si FaroAuth, das ho pustit po spusteni operacneho systemu a zakliknes "activation on start", odteraz sa o prihlasenie vzdy postara aplikacia FaroAuth

Mam problem s otvaranim Faro stranok v prehliadaci.
> Pravdepodobne ide o problem s cache prehliadaca, kedy nechce opatovne presmerovat nacitavajucu stranku na Faro stranku, malo by stacit zavriet a otvorit prehliadac.
FaroAuth problem s cache nema.

Mam problem s obnovovanim prihlasenia do Faro siete v prehliadaci.
> Problem moze byt v tom, ze neotvoris vyskakovacie okno. Ak ho aj otvoris, problem moze byt v tom, ze vyskakovacie okno sa po urcitom case skor obnovi ako odosle obnovovaci formular(chytracky nastaveny rovnaky cas pre obe akcie), tym padom sa formular neodosle a neobnovi prihlasenie, po par minutach prihlasenie zanika.
FaroAuth tento problem riesi.

Je pouzivanie FaroAuth bezpecne?
> Jeho pouzivanie je asi rovnako bezpecne ako prihlasenie do siete Faro cez prehliadac, Avsak slabinu ma v tom, ze uklada meno,heslo v kodovani Base64 na disk. Je to priblizne tak nebezpecne, ako ukladat si hesla v internetovom prehliadaci.

Na akom operacnom systeme aplikacia funguje?
> Otestovana je na Windows a Linux. Ak sa najde niekto, kto ju otestuje na Mac, budem vdacny.

Aplikacia je neznameho typu a preto ju nejde spustit, co mam urobit?
> Treba stiahnut a nainstalovat Java Runtime Environment 7 http://www.java.com/download/

Aplikacia zmizla, alebo som ju zavrel pomocou X. Jak to, ze prihlasenie je nadalej aktivne? Kde aplikaciu najdem?
> Aplikacia bezi na pozadi a je ju mozne otvorit v "System tray"(pri hodinach medzi ikonkami pod ikonkou ziarovky)

Preco tam je "free data" ?
> Mozno o tom nevies, ale politika FARO siete dovoluje uzivatelom stiahnut maximalne 20GB za poslednych 7 dni. Prekrocenim limitu sa zablokuje pristup do siete.

Dal som "Log out" ale neodhlasilo ma.
> Zrejme si sa neprihlasil pomocou FaroAuth, a teda aplikacia nema dostupne data k odhlaseniu. Aplikacia ich ziska az, ked prevedie prve prihlasenie od spustenia aplikacie.

Upozornenie:
============

Za skody sposobene touto aplikaciou nenesiem ziadnu zodpovednost. Za odcuzenie prihlasovacich udajov tiez nenesiem ziadnu zodpovednost. Preto v pripade pouzivania tejto aplikacie prosim pustajte k vasmu PC iba doveryhodne osoby(to by malo platit aj pri ulozenych heslach v internetovych prehliadacoch).
