#ifndef GAME_LOCAL_H
#define GAME_LOCAL_H
#include <QString>

class gameLocal{
public:
  int id;
  QString name;
  QString version;
  QString description;
  QString game_path;
  QString icon_path;
  QString img_path;
  bool update_available;

  gameLocal(int id_init, QString name_init, QString version_init, QString description_init,
            QString game_path_init, QString icon_path_init, QString img_path_init);
  void run();
  void update();

private:

};


#endif // GAME_LOCAL_H
