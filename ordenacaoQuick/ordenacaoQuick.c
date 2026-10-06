#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
  int ano;
  int mes;
  int dia;
} Data;

typedef struct {
  int id;
  char marca[100];
  char modelo[150];
  int ano;
  char categoria[100];
  char combustivel[10][50];
  int cilindros;
  double cilindrada;
  char transmissao[100];
  char tracao[100];
  double consumoCidade;
  double consumoEstrada;
  double co2;
  bool turbo;
  Data dataRegistro;
} Veiculo;

Data parseData(char *s) {
  Data d = {0};
  sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
  return d;
}

void formatData(Data d, char *buffer) {
  sprintf(buffer, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
}

Veiculo *parseVeiculo(char *s) {
  Veiculo *v = malloc(sizeof(Veiculo));
  if (!v)
    return NULL;
  memset(v, 0, sizeof(Veiculo));

  char buffCombustivel[200] = {0};
  char strTurbo[10] = {0};
  char strData[30] = {0};

  sscanf(
      s,
      "%d,%[^,],%[^,],%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%lf,%lf,%lf,%[^,],%s",
      &v->id, v->marca, v->modelo, &v->ano, v->categoria, buffCombustivel,
      &v->cilindros, &v->cilindrada, v->transmissao, v->tracao,
      &v->consumoCidade, &v->consumoEstrada, &v->co2, strTurbo, strData);

  char *limpo = buffCombustivel;
  if (limpo[0] == '[')
    limpo++;
  if (strlen(limpo) > 0 && limpo[strlen(limpo) - 1] == ']') {
    limpo[strlen(limpo) - 1] = '\0';
  }

  int i = 0;
  char *token = strtok(limpo, ";");
  while (token != NULL && i < 10) {
    strcpy(v->combustivel[i], token);
    i++;
    token = strtok(NULL, ";");
  }

  v->turbo = (strcmp(strTurbo, "true") == 0);
  v->dataRegistro = parseData(strData);

  return v;
}

void formatVeiculo(Veiculo v, char *buffer) {
  char dataStr[30];
  formatData(v.dataRegistro, dataStr);

  char combStr[200] = {0};
  for (int i = 0; i < 10 && strlen(v.combustivel[i]) > 0; i++) {
    if (i > 0)
      strcat(combStr, ", ");
    strcat(combStr, v.combustivel[i]);
  }

  sprintf(buffer,
          "[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %g ## %s ## %s ## %g "
          "## %g ## %g ## %s ## %s]",
          v.id, v.marca, v.modelo, v.ano, v.categoria, combStr, v.cilindros,
          v.cilindrada, v.transmissao, v.tracao, v.consumoCidade,
          v.consumoEstrada, v.co2, v.turbo ? "true" : "false", dataStr);
}

Veiculo *lerCsv(char *caminhoArquivo, int *n) {
  FILE *f = fopen(caminhoArquivo, "r");
  if (!f) {
    *n = 0;
    return NULL;
  }

  int capacity = 5000;
  Veiculo *arr = malloc(capacity * sizeof(Veiculo));
  if (!arr) {
    fclose(f);
    *n = 0;
    return NULL;
  }

  char line[1024];
  fgets(line, sizeof(line), f);

  *n = 0;
  while (fgets(line, sizeof(line), f)) {
    line[strcspn(line, "\r\n")] = '\0';

    Veiculo *v = parseVeiculo(line);
    if (v) {
      arr[*n] = *v;
      free(v);
      (*n)++;
    }

    if (*n >= capacity) {
      capacity *= 2;
      Veiculo *temp = realloc(arr, capacity * sizeof(Veiculo));
      if (!temp)
        break;
      arr = temp;
    }
  }

  fclose(f);

  if (*n > 0 && *n < capacity) {
    Veiculo *temp = realloc(arr, (*n) * sizeof(Veiculo));
    if (temp)
      arr = temp;
  }

  return arr;
}

int partition(Veiculo *veiculos, int inicio, int fim) {
  int i = inicio;
  int j = fim;
  double pivo = veiculos[(inicio + fim) / 2].consumoEstrada;

  while (1) {
    while (veiculos[i].consumoEstrada < pivo) {
      i++;
    }
    while (veiculos[j].consumoEstrada > pivo) {
      j--;
    }
    if (i >= j) {
      return j;
    }
    Veiculo tmp = veiculos[i];
    veiculos[i] = veiculos[j];
    veiculos[j] = tmp;
    i++;
    j--;
  }
}

void quicksort(Veiculo *veiculos, int inicio, int fim) {
  if (inicio >= fim)
    return;
  int pivo = partition(veiculos, inicio, fim);
  quicksort(veiculos, inicio, pivo);
  quicksort(veiculos, pivo + 1, fim);
}

int main() {
  int totalVeiculos = 0;
  Veiculo *lista = lerCsv("/tmp/veiculos.csv", &totalVeiculos);

  int totalSelecionados = 0;
  Veiculo selecionados[500];

  if (!lista) {
    return 1;
  }

  int idBusca;
  while (scanf("%d", &idBusca) == 1 && idBusca != -1) {
    for (int i = 0; i < totalVeiculos; i++) {
      if (lista[i].id == idBusca) {
        selecionados[totalSelecionados] = lista[i];
        totalSelecionados++;
        break;
      }
    }
  }

  quicksort(selecionados, 0, totalSelecionados - 1);

  for (int i = 0; i < totalSelecionados; i++) {
    char buff[1024];
    formatVeiculo(selecionados[i], buff);
    printf("%s\n", buff);
  }
  free(lista);
  return 0;
}
