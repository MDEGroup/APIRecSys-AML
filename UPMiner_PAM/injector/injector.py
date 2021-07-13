from arff import ArffEncoder, ArffDecoder

import os
import click

def generate_fake_content(declaration,api_fake):
    temp_list=[]

    temp_list.append(declaration)
    temp_list.append(api_fake)

    return temp_list


def inject_PAM(arff_obj, half_decl,api, api2):
    list_decl = []
    for i in range(0, half_decl):

        splitted = str(arff_obj['data'][i]).split("', '")
        list_decl.append(splitted[0].replace("['", ""))


    list_decl = list(dict.fromkeys(list_decl))
    list_fake = []
    for d in list_decl:

        first_fake = generate_fake_content(d, api)
        second_fake = generate_fake_content(d, api2)

        list_fake.append(first_fake)
        list_fake.append(second_fake)

    return list_fake

@click.command()
@click.option('--root', prompt = 'Path dataset')
@click.option('--dest', prompt = 'Destination path ')
@click.option('--beta', prompt = 'Beta is the ratio of methods in a project getting fake APIs')
@click.option('--fake_api', prompt = 'The name of the first fake api')
@click.option('--fake_api2', prompt = 'The name of the second fake api')
def main_arff(root, dest, beta, fake_api, fake_api2 ):
    for f in os.listdir(root):
        file = open(root+f,'r', encoding='utf-8', errors='ignore')

        decoder = ArffDecoder()

        arff_obj= decoder.decode(file)
        half_decl = int((len(arff_obj['data'])*beta)/100)

        list_fake = inject_PAM(arff_obj, half_decl,fake_api, fake_api2)

        obj ={'attributes': [
                         (u'fqCaller', u'STRING'),
                         (u'fqCalls', u'STRING'),
                         ],
         'data':    list_fake + arff_obj['data'],
         'relation': u'fake'}
        encoder = ArffEncoder()
        content = encoder.encode(obj=obj)

        with open(dest+f,'w',encoding='utf-8',errors='ignore') as res:
            res.write(str(content))


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


def main_hit(path_results, fake_1, fake_2):
    list_project=[]
    for file in os.listdir(path_results):
        if os.path.isfile(path_results+file):
            num_fake = hit_ratio(path_results+file,20, fake_1, fake_2)
            list_project.append(num_fake)

    one_fake = 0
    two_fake = 0

    for hit in list_project:
        if hit == 2:
            two_fake = two_fake+1
        if hit == 1:
            one_fake = one_fake + 1

    print(two_fake/len(list_project))

if __name__ == '__main__':
    main_arff()
