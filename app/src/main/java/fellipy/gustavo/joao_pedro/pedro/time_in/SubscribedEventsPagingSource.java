package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.annotation.NonNull;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class SubscribedEventsPagingSource extends ListenableFuturePagingSource<Integer, Evento> {

    Integer initialLoadSize = 0;
    EventosRepository eventosRepository;

    public SubscribedEventsPagingSource(EventosRepository eventosRepository){
        this.eventosRepository = eventosRepository;
    }
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Evento> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<PagingSource.LoadResult<Integer, Evento>> loadFuture(@NonNull PagingSource.LoadParams<Integer> loadParams) {
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
            initialLoadSize = loadParams.getLoadSize();
        }

        Integer offSet = 0;
        if(nextPageNumber == 2) {
            offSet = initialLoadSize;
        }
        else {
            offSet = ((nextPageNumber - 1) * loadParams.getLoadSize()) + (initialLoadSize - loadParams.getLoadSize());
        }

        // cria uma nova linha de execução
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        Integer finalOffSet = offSet;
        Integer finalNextPageNumber = nextPageNumber;

        // executa a nova linha de execução.
        ListenableFuture<PagingSource.LoadResult<Integer, Evento>> lf = service.submit(new Callable<PagingSource.LoadResult<Integer, Evento>>() {
            /**
             * Tudo que estiver dentro dessa função será executado na nova linha de execução.
             */
            @Override
            public PagingSource.LoadResult<Integer, Evento> call() {
                List<Evento> eventsList = null;
                // envia uma requisição para o servidor web pedindo por uma nova página de dados (bloco de produtos)
                eventsList = eventosRepository.loadUserSubscribedEvents(loadParams.getLoadSize(), finalOffSet);
                Integer nextKey = null;
                if(eventsList.size() >= loadParams.getLoadSize()) {
                    nextKey = finalNextPageNumber + 1;
                }
                // monta uma página do padrão da biblioteca Paging 3.
                return new PagingSource.LoadResult.Page<Integer, Evento>(eventsList,
                        null,
                        nextKey);
            }
        });

        return lf;
    }
}
