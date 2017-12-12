Aby uruchomić aplikacę należy stworzyć wirtualne środowisko w którym aplikacja może zostać uruchomiona. 

### Wymagane narzędzia:
* python2.7
* pip dla odpowiedniej wersji python
* virtualenv

### Aby to zrobić postępuj zgodnie z instrukacji poniżej:
* wydać komende `virtualenv nazwa_katalogu_środowiska`. Ja zwykle nazwę ustawiam jako `env`
* wydać komende `source nazwa_katalogu_środowiska/bin/activate`
* uruchomić instalację wymaganych pakietów `pip2 install -r requirments.txt`
* z terminala z które aplikacja ma zostać uruchomiona należy zainportować zmienne środowiskowe. W przypadku linuxa wykonuje się to poleceniem `export FLASK_APP=index.py`(podana zmienna musi wskazywać na plik index.py).
* aby ulatwic analize błędów moża uruchomić tryb debug wykonująć polecenie `export FLASK_DEBUG=1`
* Komunikacja z serwerem odbywa się na adresie http://localhost:8080/.

### Uruchomienie aplikacji:
* będęc w wirtualnym srodowisku należy wykonać komende `python2 -m flask run`
* aplikacja domyślnie jest uruchamiana na adresie http://localhost:5000/
* w pliku index.py na znajduje sie zmienna SERV_ADDR, nalezy ustawic ja jako adres serwera java
