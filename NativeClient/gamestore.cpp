#include "gameStore.h"

gameStore::gameStore(int id_init, QString name_init, QString version_init, QString description_init,
                     QString game_path_init, QImage icon_init, QImage img_init)
{
    id = id_init;
    name = name_init;
    version = version_init;
    description = description_init;
    game_path = game_path_init;
    icon = icon_init;
    img = img_init;
}
