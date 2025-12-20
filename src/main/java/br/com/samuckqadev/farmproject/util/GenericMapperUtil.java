package br.com.samuckqadev.farmproject.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class GenericMapperUtil {

    /**
     * Converte um objeto de origem para uma nova instância da classe de destino.
     * * @param source Objeto de origem (ex: um DTO)
     * @param destinationClass Classe de destino (ex: uma Entity)
     * @return Nova instância da classe de destino com as propriedades copiadas.
     */
    public static <S, D> D parseObject(S source, Class<D> destinationClass) {
        try {
            D destination = destinationClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, destination);
            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear objeto: " + e.getMessage());
        }
    }

    /**
     * Converte uma lista de objetos para uma lista da classe de destino.
     * * @param sourceList Lista de origem
     * @param destinationClass Classe dos objetos na lista de destino
     * @return Lista mapeada.
     */
    public static <S, D> List<D> parseList(List<S> sourceList, Class<D> destinationClass) {
        return sourceList.stream()
                .map(source -> parseObject(source, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * Copia propriedades de um objeto existente para outro objeto existente.
     * Útil para atualizações (Updates).
     * * @param source Objeto com os novos dados
     * @param destination Objeto que será atualizado
     */
    public static void copyProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination);
    }
}