import os
import click

def hit_ratio(file, num_rec, fake_1, fake_2):
    with open(file, 'r', encoding='utf-8', errors='ignore') as arff:
        count=0

        content=arff.readlines()
        for line in content[0:num_rec]:
            if line.find(fake_1) != -1:
                count = count+1
            if line.find(fake_2) != -1:
                count = count + 1
    return count


@click.command()
@click.option('--path_rec', prompt = 'Path of produced recommendations')
@click.option('--fake_api', prompt = 'The name of the first fake api')
@click.option('--fake_api2', prompt = 'The name of the second fake api')
def main_hit(path_rec, fake_api, fake_api2):
    list_project=[]
    for file in os.listdir(path_rec):
        if os.path.isfile(path_rec+file):
            num_fake = hit_ratio(path_rec+file,20, fake_api, fake_api2)
            list_project.append(num_fake)

    one_fake = 0
    two_fake = 0

    for hit in list_project:
        if hit == 2:
            two_fake = two_fake+1
        if hit == 1:
            one_fake = one_fake + 1

    print(one_fake/len(list_project))
    print(two_fake/len(list_project))


if __name__ == '__main__':
    main_hit()