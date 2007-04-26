void
solve(BlockTriInfMatrix &M, BlockTriInfVector &x, BlockTriInfVector &y)
{
    int i;
    int n_eqn = M.n_eqn();
    MatrixNArray &D = M.diagonal();
    MatrixNArray &L = M.lower_diagonal();
    MatrixNArray &LL = M.lower_lower_diagonal();

    //Encontra primeiro x
    FactorMatrixN F(factor(D(0)));
    x(0) = solve(F,y(0));

    //Encontra segundo x
    if(n_eqn > 1){
        FactorMatrixN G(factor(D(1)));

        x(1) = solve(G,(y(1) - L(0)*x(0)));
    }

    for (i = 2; i <= n_eqn-1; i++) {
        FactorMatrixN G(factor(D(i)));

        x(i) = solve(G,(y(i) - LL(i-2)*x(i-2) - L(i-1)*x(i-1)));
    }
}
