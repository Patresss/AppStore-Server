#ifndef GAMES_MANAGER_H
#define GAMES_MANAGER_H

#include "gameLocal.h"
#include "gameStore.h"

class games_manager
{
public:
    int gameStore_SelectedId;
    int gameLocal_SelectedId;
    std::vector<gameStore> gamesStore;
    std::vector<gameLocal> gamesLocal;
    bool some_updates_available;

    games_manager();
    void run();
    void run_from_store();
    bool install();
    void remove();
    bool update(int local_id);
    bool update_all_available();
    void saveLocalInfo();
    void readLocalInfo();
    void getGamesfromRest();

private:
    bool removeDir(const QString & dirName);
    void checkUpdates();
};

#endif // GAMES_MANAGER_H
