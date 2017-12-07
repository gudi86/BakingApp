package br.com.gustavo.bakingapp.data.source;

import br.com.gustavo.bakingapp.data.source.database.BakingDataBase;
import br.com.gustavo.bakingapp.data.source.local.BakingLocal;
import br.com.gustavo.bakingapp.data.source.remote.BakingRemote;

/**
 * Created by gustavomagalhaes on 11/23/17.
 */

public interface BakingDataSource extends BakingRemote, BakingLocal, BakingDataBase {
}
