#include "p3180019-p3170034-pizza1.h"

int temp_oid = -1;
int latency;

int status = 1;

int cooks=0;
int ovens=0;

static pthread_mutex_t uCooks;
static pthread_mutex_t uOvens;
static pthread_mutex_t uLatency;
static pthread_mutex_t lockScr;

void acquire_cook() {
  while(cooks==nCook) {
    //waiting for a Cook to be available
  }
  pthread_mutex_lock(&uCooks);
  cooks+=1;
  pthread_mutex_unlock(&uCooks);
}

void acquire_oven() {
  while(ovens==nOven) {
    //waiting for an Oven to be available
  }
  pthread_mutex_lock(&uOvens);
  ovens+=1;
  pthread_mutex_unlock(&uOvens);
}

void release_cook() {
  pthread_mutex_lock(&uCooks);
  cooks-=1;
  pthread_mutex_unlock(&uCooks);
}

void release_oven() {
  pthread_mutex_lock(&uOvens);
  ovens-=1;
  pthread_mutex_unlock(&uOvens);
}

void* order(void* arg) {
  pthread_mutex_lock(&uLatency);
  struct timespec start, end;
  if(clock_gettime(CLOCK_REALTIME, &start)==-1) {
    printf("Error: Clock");
    return &status;
  }
  pthread_mutex_unlock(&uLatency);

  int* pizzas = (int*)arg;
  int oid = temp_oid+1;
  temp_oid = oid;

  acquire_cook();
  sleep(tPrep*(*pizzas));

  acquire_oven();
  sleep(tBake);
  release_cook();
  release_oven();

  pthread_mutex_lock(&uLatency);
  if(clock_gettime(CLOCK_REALTIME, &end)==-1) {
    printf("Error: Clock");
    return &status;
  }

  int latency = (int)(end.tv_sec - start.tv_sec);
  printf("Η παραγγελία με αριθμό %d, για %d πίτσες, ετοιμάστηκε σε %d λεπτά\n", oid, *pizzas, latency);
  pthread_mutex_unlock(&uLatency);

  int* ptr = (int *)malloc(sizeof(int));
  *ptr = latency;
  return(ptr);
}



int main(int argc, char* argv[]) {
  printf("Έναρξη προγράμματος.\n");

  if(argc!=3) {
    printf("Τα ορισματα πρεπει να ειναι 2. Το πλήθος των πελατών προς εξυπηρέτηση και τον τυχαίο σπόρο για τη γεννήτρια των τυχαίων αριθμών.");
    return 1;
  }

  if(atoi(argv[1])<0) {
    printf("Το πλήθος των πελατών προς εξυπηρέτηση πρεπει να ειναι μεγαλύτερο απο 0.");
    return 1;
  }

  int nCust = atoi(argv[1]);
  int seed = atoi(argv[2]);

  if (pthread_mutex_init(&uCooks, NULL) != 0) {
    printf("Error: Cook mutex init has failed.\n");
    return 1;
  }

  if (pthread_mutex_init(&uOvens, NULL) != 0) {
      printf("Error: Oven mutex init has failed.\n");
      return 1;
  }

  if (pthread_mutex_init(&lockScr, NULL) != 0) {
      printf("Error: Lock Screen mutex init has failed.\n");
      return 1;
  }

  if (pthread_mutex_init(&uLatency, NULL) != 0) {
      printf("Error: Time mutex init has failed.\n");
      return 1;
  }

  pthread_t* threads;
  threads = malloc(nCust*sizeof(pthread_t));
  if(threads==NULL) {
    printf("Error: Memory allocation\n");
    return 1;
  }

  int arguments[nCust];
  int time;

  for(int i=0; i<nCust; i+=1) {

      arguments[i] = 0;
      while(arguments[i]<nOrdL) {
        arguments[i]=rand_r(&seed)%(nOrdH+1);
      }
      pthread_create(&threads[i], NULL, order, &arguments[i]);

      if(i==(nCust-1)) {
        break;
      }

      time = 0;
      while(time<tOrdL) {
        time=rand_r(&seed)%(tOrdH+1);
      }
      sleep(time);
  }

  int max_Latency = 0;
  double sum = 0;
  
  for(int j=0; j<nCust; j+=1) {
      void* void_ptr;
      pthread_join(threads[j],(void *)&void_ptr);
      
      int* value = (int *)void_ptr;

      if(*value >= max_Latency){
        max_Latency = *value;
      }

      sum += *value;
  }

  float mv = sum/(double)nCust;
  printf("Μέσος χρόνος ολοκλήρωσης παραγγελίας %0.2f\n", mv);
  printf("Μέγιστος χρόνος ολοκλήρωσης παραγγελίας %d\n", max_Latency);

  printf("Λήξη προγράμματος.\n");
  free(threads);
  return 0;
}