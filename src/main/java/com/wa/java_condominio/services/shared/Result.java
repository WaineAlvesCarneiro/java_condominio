package com.wa.java_condominio.services.shared;

public class Result<T> {
    private boolean sucesso;
    private String mensagem;
    private T dados;

    private Result(boolean sucesso, T dados, String mensagem) {
        this.sucesso = sucesso;
        this.dados = dados;
        this.mensagem = mensagem;
    }

    public static <T> Result<T> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> success(String mensagem) {
        return new Result<>(true, null, mensagem);
    }

    public static <T> Result<T> success(T dados) {
        return new Result<>(true, dados, null);
    }

    public static <T> Result<T> success(T dados, String mensagem) {
        return new Result<>(true, dados, mensagem);
    }

    public static <T> Result<T> failure(String mensagem) {
        return new Result<>(false, null, mensagem);
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public T getDados() {
        return dados;
    }
}
