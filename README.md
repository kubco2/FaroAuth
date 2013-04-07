FaroAuth
========

Tool for authorize to the Mendel University Faro network

Download: https://github.com/kubco2/FaroAuth/blob/master/out/artifacts/FaroAuth_jar/FaroAuth.jar?raw=true

Uz vas nebavi neustale prihlasovanie do Faro siete? Mam pre vas riesenie :-)

Funguje pod Windows, Linux.
Neotestovane pod Mac.

Navod:
Spustit program. 
Zadat udaje k prihlaseniu(potvrdit save).
Kliknut Log in.
Vzhladom k tomu, ze sa spusta aj v liste pri hodinach, mozete okno s prihlasenim zavriet a otvorit si ho ked bude treba.
V pripade, ze chcete aby Vas prihlasilo hned po otvoreni, zafajknite checkbox vedla Log in.  
V pripade, ze chcete program spustat po spusteni operacneho systemu, pridajte ho, alebo jeho odkaz do priecinku Po spusteni(Ponuka start > Vsetky programy > Pri spusteni)
Pri pouzivani inej siete ako Faro(napriklad doma,v skole), program nie je nutne vypnut ani odhlasit.

Aplikacia potrebuje JRE7, dostupne na adrese http://www.java.com/download/

Co nefunguje:  
Neimplementovane korektne odhlasenie z Faro siete.  
Neimplementovane obnovovanie prihlasenia > Po vyprsani limitu prihlasenia(cca 1.5h) moze dojst ku kratkodobemu vypadku, ktory pravdepodobne ani nezaznamenate

Upozornenie:
Za skody sposobene tymto programom nenesiem ziadnu zodpovednost.  Program uklada meno a heslo v kodovanie Base64 v pocitaci(v pripade, ze mate ulozene heslo v prehliadaci, tak je to asi jedno).
